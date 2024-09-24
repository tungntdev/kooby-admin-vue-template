<script setup>
import { inject, ref } from 'vue';
import ReportService from '@/service/ReportService';
import SETTINGS from '@/constants/settings';
import { doReadNumber, ReadingConfig } from 'read-vietnamese-number';
import PizZip from 'pizzip';
import Docxtemplater from 'docxtemplater';
import App from '@/constants/app';

const startDate = ref(new Date());
const endDate = ref(new Date());

const listDelivery = ref([]);
const reportService = ReportService.INSTANCE;
const $loading = inject('$loading');
const sumDeliveryCost = ref(0);

async function getPatientDelivery() {
    let params = {
        startDate: startDate.value,
        endDate: endDate.value
    };
    const loader = $loading.show(SETTINGS.LOADING_PROPERTIES);
    try {
        const res = await reportService.deliveryReport(params);
        if (res.state) {
            listDelivery.value = res.payload;

            sumDeliveryCost.value = listDelivery.value.reduce((total, item) => {
                return total + (item.deliveryCost || 0);
            }, 0);
        }
    } finally {
        setTimeout(() => loader.hide(), 1000);
    }
}

const formatDate = (value) => {
    if (value) {
        const newDate = new Date(value);
        return newDate.toLocaleDateString('en-GB', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        });
    }
};
const config = new ReadingConfig();
config.unit = ['đồng'];

function convertNumberToWord(cost) {
    return doReadNumber(config, cost.toString());
}

async function generateWordFile() {
    const docxUrl = '../public/templateFile/MauBaoCaoNhanChuyenPhat.docx';

    const templateData = await fetch(docxUrl).then((res) => res.arrayBuffer());

    const zip = new PizZip(templateData);
    const doc = new Docxtemplater(zip);

    const data = {
        SoPhieu: new Date().getFullYear().toString() + (new Date().getMonth() + 1).toString() + new Date().getDate().toString(),
        TuNgay: formatDate(startDate.value),
        DenNgay: formatDate(endDate.value),
        NgayNhanChuyen: formatDate(startDate.value) + ' - ' + formatDate(endDate.value),
        TongCong: sumDeliveryCost.value.toLocaleString(),
        SoTienBangChu: convertNumberToWord(sumDeliveryCost.value),
        NgayIn: 'Hà Nội, ngày ' + new Date().getDate() + ' tháng ' + (new Date().getMonth() + 1) + ' năm ' + new Date().getFullYear(),
        NguoiThuTien: localStorage.getItem(App.PROFILE.FULL_NAME),
        items: listDelivery.value
    };

    data.items = data.items.map((item) => {
        return {
            STT: item.order,
            SoPhieu: item.numberOrder,
            HoTen: item.patientName,
            Khoa: item.department,
            NgayVao: formatDate(item.inDate),
            NgayRa: formatDate(item.outDate),
            NhanSao: formatDate(item.receptionDate),
            HenTra: formatDate(item.appointmentPatientDate),
            PhiChuyen: item.deliveryCost
        };
    });
    doc.setData(data);

    doc.render();

    const out = doc.getZip().generate({
        type: 'blob',
        mimeType: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
    });

    const url = URL.createObjectURL(out);
    const link = document.createElement('a');
    link.href = url;
    link.download = 'Danh sách nhận chuyển bưu sao bệnh án ' + data.SoPhieu + ' result.docx';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
}
</script>

<template>
    <div class="grid">
        <div class="col-12">
            <div class="card">
                <h5>{{ $tt('report-delivery.header') }}</h5>
                <Toolbar>
                    <template v-slot:start>
                        <label for="startDate">{{ $tt('report-delivery.tool.start-date') }}</label>
                        <DatePicker v-model="startDate" class="ml-2 mr-5" dateFormat="dd/mm/yy" showIcon />
                        <label for="endDate">{{ $tt('report-delivery.tool.end-date') }}</label>
                        <DatePicker v-model="endDate" class="ml-2 mr-5" dateFormat="dd/mm/yy" showIcon />
                    </template>
                    <template v-slot:end>
                        <Button class="mr-2" icon="pi pi-search" :label="$tt('report-delivery.tool.button.search')" @click="getPatientDelivery()" />
                        <Button class="p-button-info mr-2" icon="pi pi-file-export" :label="$tt('report-delivery.tool.button.file-export')" @click="generateWordFile" />
                    </template>
                </Toolbar>
            </div>
        </div>
        <div class="col-12">
            <div class="card">
                <DataTable :scrollable="true" :value="listDelivery" class="mt-3" scrollDirection="both" scrollHeight="540px">
                    <template #header>
                        <div class="flex flex-wrap align-items-center justify-content-between gap-2">
                            <span class="text-xl text-900 font-bold">{{ $tt('report-delivery.table.header') }} {{ sumDeliveryCost.toLocaleString() }} đồng</span>
                        </div>
                    </template>
                    <Column :style="{ width: '50px' }" :header="$tt('report-delivery.table.order')">
                        <template #body="{ data }">
                            {{ data.order }}
                        </template>
                    </Column>
                    <Column :style="{ width: '100px' }" :header="$tt('report-delivery.table.number-order')">
                        <template #body="{ data }">
                            {{ data.numberOrder }}
                        </template>
                    </Column>
                    <Column :style="{ width: '250px' }" :header="$tt('report-delivery.table.patient-name')">
                        <template #body="{ data }">
                            {{ data.patientName }}
                        </template>
                    </Column>
                    <Column :style="{ width: '100px' }" :header="$tt('report-delivery.table.department')">
                        <template #body="{ data }">
                            {{ data.department }}
                        </template>
                    </Column>
                    <Column :style="{ width: '200px' }" :header="$tt('report-delivery.table.in-date')">
                        <template #body="{ data }">
                            {{ formatDate(data.inDate) }}
                        </template>
                    </Column>
                    <Column :style="{ width: '200px' }" :header="$tt('report-delivery.table.out-date')">
                        <template #body="{ data }">
                            {{ formatDate(data.outDate) }}
                        </template>
                    </Column>
                    <Column :style="{ width: '200px' }" :header="$tt('report-delivery.table.reception-date')">
                        <template #body="{ data }">
                            {{ formatDate(data.receptionDate) }}
                        </template>
                    </Column>
                    <Column :style="{ width: '200px' }" :header="$tt('report-delivery.table.appointment-date')">
                        <template #body="{ data }">
                            {{ formatDate(data.appointmentPatientDate) }}
                        </template>
                    </Column>
                    <Column :style="{ width: '200px' }" :header="$tt('report-delivery.table.copy-cost')">
                        <template #body="{ data }">
                            {{ data.deliveryCost.toLocaleString() }}
                        </template>
                    </Column>
                </DataTable>
            </div>
        </div>
    </div>
</template>

<style scoped lang="scss"></style>
