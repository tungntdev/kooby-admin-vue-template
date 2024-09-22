<script setup>
import { inject, ref } from 'vue';
import ReportService from '@/service/ReportService';
import SETTINGS from '@/constants/settings';
import PizZip from 'pizzip';
import Docxtemplater from 'docxtemplater';
import { doReadNumber, ReadingConfig } from 'read-vietnamese-number';

const startDate = ref(new Date());
const endDate = ref(new Date());

const listPatientRequest = ref([]);
const reportService = ReportService.INSTANCE;
const $loading = inject('$loading');
const sumCost = ref(0);

async function getPatientRequest() {
    let params = {
        startDate: startDate.value,
        endDate: endDate.value
    };
    const loader = $loading.show(SETTINGS.LOADING_PROPERTIES);
    try {
        const res = await reportService.patientRequestReport(params);
        if (res.state) {
            listPatientRequest.value = res.payload;

            sumCost.value = listPatientRequest.value.reduce((total, item) => {
                return total + (item.copyCost || 0);
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
    const docxUrl = '../public/templateFile/MauBaoCaoTaiChinh.docx';

    const templateData = await fetch(docxUrl).then((res) => res.arrayBuffer());

    // Tạo một đối tượng PizZip và đối tượng Docxtemplater
    const zip = new PizZip(templateData);
    const doc = new Docxtemplater(zip);

    // Dữ liệu để điền vào mẫu Word
    const data = {
        SoPhieu: new Date().getFullYear().toString() + (new Date().getMonth() + 1).toString() + new Date().getDate().toString(),
        TuNgay: formatDate(startDate.value),
        DenNgay: formatDate(endDate.value),
        TongCong: sumCost.value.toLocaleString(),
        SoTienBangChu: convertNumberToWord(sumCost.value),
        NgayIn: 'Hà Nội, ngày ' + new Date().getDate() + ' tháng ' + (new Date().getMonth() + 1) + ' năm ' + new Date().getFullYear(),
        NguoiThuTien: localStorage.getItem('fullname'),
        items: listPatientRequest.value
    };

    console.debug(data);

    data.items = data.items.map((item) => {
        return {
            STT: item.order,
            SoPhieu: item.numberOrder,
            HoTen: item.patientName,
            Khoa: item.department,
            NgayVao: formatDate(item.inDate),
            NgayRa: formatDate(item.outDate),
            NgayThu: formatDate(item.receptionDate),
            ChiPhi: item.copyCost.toLocaleString()
        };
    });
    // Điền dữ liệu vào mẫu Word
    doc.setData(data);

    // Kết xuất mẫu Word đã điền dữ liệu
    doc.render();

    const out = doc.getZip().generate({
        type: 'blob',
        mimeType: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
    });

    // Tạo URL từ Blob và tự động tải xuống
    const url = URL.createObjectURL(out);
    const link = document.createElement('a');
    link.href = url;
    link.download = 'Doanh thu sao bệnh án ' + data.SoPhieu + ' result.docx';
    document.body.appendChild(link);
    link.click();

    // Dọn dẹp
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
}
</script>

<template>
    <div class="grid">
        <div class="col-12">
            <div class="card">
                <h1 class="mb-4">{{ $tt('report-request.header') }}</h1>
                <Toolbar>
                    <template v-slot:start>
                        <label for="startDate">{{ $tt('report-request.tool.start-date') }}</label>
                        <DatePicker v-model="startDate" class="ml-2 mr-5" dateFormat="dd/mm/yy" showIcon />
                        <label for="endDate">{{ $tt('report-request.tool.end-date') }}</label>
                        <DatePicker v-model="endDate" class="ml-2 mr-5" dateFormat="dd/mm/yy" showIcon />
                    </template>
                    <template v-slot:end>
                        <Button class="mr-2" icon="pi pi-search" :label="$tt('report-request.tool.button.search')" @click="getPatientRequest()" />
                        <Button class="p-button-info mr-2" icon="pi pi-file-export" :label="$tt('report-request.tool.button.file-export')" @click="generateWordFile()" />
                    </template>
                </Toolbar>
            </div>
        </div>
        <div class="col-12">
            <div class="card">
                <DataTable :scrollable="true" :value="listPatientRequest" class="mt-3" scrollDirection="both" scrollHeight="540px">
                    <template #header>
                        <div class="flex flex-wrap align-items-center justify-content-between gap-2">
                            <span class="text-xl text-900 font-bold">
                                {{ $tt('report-request.table.header') }}
                                <span style="color: red; font-weight: bold"> {{ sumCost.toLocaleString() }} đồng </span>
                            </span>
                        </div>
                    </template>
                    <Column :style="{ width: '50px' }" :header="$tt('report-request.table.order')">
                        <template #body="{ data }">
                            {{ data.order }}
                        </template>
                    </Column>
                    <Column :style="{ width: '100px' }" :header="$tt('report-request.table.number-order')">
                        <template #body="{ data }">
                            {{ data.numberOrder }}
                        </template>
                    </Column>
                    <Column :style="{ width: '250px' }" :header="$tt('report-request.table.patient-name')">
                        <template #body="{ data }">
                            {{ data.patientName }}
                        </template>
                    </Column>
                    <Column :style="{ width: '100px' }" :header="$tt('report-request.table.department')">
                        <template #body="{ data }">
                            {{ data.department }}
                        </template>
                    </Column>
                    <Column :style="{ width: '200px' }" :header="$tt('report-request.table.in-date')">
                        <template #body="{ data }">
                            {{ formatDate(data.inDate) }}
                        </template>
                    </Column>
                    <Column :style="{ width: '200px' }" :header="$tt('report-request.table.out-date')">
                        <template #body="{ data }">
                            {{ formatDate(data.outDate) }}
                        </template>
                    </Column>
                    <Column :style="{ width: '200px' }" :header="$tt('report-request.table.reception-date')">
                        <template #body="{ data }">
                            {{ formatDate(data.receptionDate) }}
                        </template>
                    </Column>
                    <Column :style="{ width: '200px' }" :header="$tt('report-request.table.copy-cost')">
                        <template #body="{ data }">
                            {{ data.copyCost.toLocaleString() }}
                        </template>
                    </Column>
                </DataTable>
            </div>
        </div>
    </div>
</template>

<style scoped lang="scss"></style>
