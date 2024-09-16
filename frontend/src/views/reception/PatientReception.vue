<script setup>
import { inject, onBeforeMount, ref } from 'vue';
import PatientRequestService from '@/service/PatientRequestService';
import Common from '@/constants/common';
import SETTINGS from '@/constants/settings';
import CreateReception from '@/views/reception/CreateReception.vue';

const patientRequestService = PatientRequestService.INSTANCE;

const keyword = ref('');
const pageNumber = ref(0);
const totalPages = ref();
const totalItems = ref();
const currentItems = ref();
const states = ref([]);
const patientRequests = ref();

const $loading = inject('$loading');

async function fetchPatientRequest() {
    let params = {
        keyword: keyword.value,
        states: states.value,
        pageNo: pageNumber.value
    };
    const loader = $loading.show(SETTINGS.LOADING_PROPERTIES);
    try {
        const res = await patientRequestService.getPatientRequests(params);
        patientRequests.value = res.payload.data;
        pageNumber.value = res.payload.currentPage;
        totalPages.value = res.payload.totalPages;
        totalItems.value = res.payload.totalItems;
    } finally {
        setTimeout(() => loader.hide(), 500);
    }
}

onBeforeMount(async () => {
    await fetchPatientRequest();
});

const changePage = async () => {
    pageNumber.value = currentItems.value / 10 + 1;
    await fetchPatientRequest();
};

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

const getSeverity = (status) => {
    switch (status) {
        case Common.STATES.PENDING:
            return 'secondary';

        case Common.STATES.ASSIGNED:
            return 'info';

        case Common.STATES.IN_PROGRESS:
            return 'warn';

        case Common.STATES.DIRECTOR_APPROVED:
            return 'warn';

        case Common.STATES.MANAGER_APPROVED:
            return 'warn';

        case Common.STATES.REJECTED:
            return 'danger';

        case Common.STATES.COMPLETE:
            return 'success';

        case null:
            return null;
    }
};

const getDeliveryState = (data) => {
    if (data.deliveryDate) {
        return formatDate(data.deliveryDate);
    } else {
        if (data.deliveryOrderNumber) {
            return 'Đăng ký';
        }
    }
};

const createRef = ref();
const nextNumberOrder = ref(0);

async function onClickNew() {
    createRef.value.visible = true;
}
</script>

<template>
    <div>
        <div class="card">
            <Toolbar class="mb-2">
                <template v-slot:start>
                    <Button type="button" :label="$tt('patientRequest.button.add')" icon="pi pi-pen-to-square" @click="onClickNew()" />
                </template>
                <template v-slot:end>
                    <Button type="button" :label="$tt('patientRequest.button.search')" icon="pi pi-search" severity="success" />
                </template>
            </Toolbar>
            <DataTable :value="patientRequests" size="small" scrollable scrollHeight="500px" tableStyle="min-width: 50rem">
                <template #header>
                    <div class="flex flex-wrap items-center justify-between gap-2">
                        <span class="text-xl font-bold">{{ $tt('patientRequest.table.title') }}</span>
                    </div>
                </template>
                <Column field="numberOrder" :header="$tt('patientRequest.table.order')" bodyClass="text-center" headerStyle="font-weight:bold" style="min-width: 50px"></Column>
                <Column field="patientName" frozen :header="$tt('patientRequest.table.name')" style="min-width: 150px"></Column>
                <Column field="department" :header="$tt('patientRequest.table.department')" style="min-width: 50px"></Column>
                <Column :header="$tt('patientRequest.table.inDate')" bodyClass="text-left" headerStyle="font-weight: bold" style="min-width: 80px">
                    <template #body="{ data }">
                        {{ formatDate(data.inDate) }}
                    </template>
                </Column>
                <Column :header="$tt('patientRequest.table.outDate')" style="min-width: 80px">
                    <template #body="{ data }">
                        {{ formatDate(data.outDate) }}
                    </template>
                </Column>
                <Column field="copyQuantity" :header="$tt('patientRequest.table.quantity')" style="min-width: 50px"></Column>
                <Column field="note" :header="$tt('patientRequest.table.note')" style="min-width: 100px"></Column>
                <Column field="stateName" :header="$tt('patientRequest.table.state')" style="min-width: 80px">
                    <template #body="{ data }">
                        <Tag :value="data.stateName" :severity="getSeverity(data.state)" />
                    </template>
                </Column>

                <Column field="stateName" :header="$tt('patientRequest.table.delivery')" style="min-width: 80px">
                    <template #body="{ data }">
                        <div v-if="data.deliveryOrderNumber">
                            <Tag :value="getDeliveryState(data)" severity="danger" />
                        </div>
                    </template>
                </Column>
                <Column field="idCopyUser" :header="$tt('patientRequest.table.copy')" style="min-width: 80px"></Column>
                <Column :header="$tt('patientRequest.table.signDate')" style="min-width: 80px">
                    <template #body="{ data }">
                        {{ formatDate(data.signDate) }}
                    </template>
                </Column>
                <Column :header="$tt('patientRequest.table.done')" style="min-width: 80px">
                    <template #body="{ data }">
                        {{ formatDate(data.donePatientDate) }}
                    </template>
                </Column>
                <Column :header="$tt('patientRequest.table.actions')" frozen alignFrozen="right" style="min-width: 50px">
                    <template #body="{ data }">
                        <Button aria-label="Actions" icon="pi pi-spin pi-cog" rounded severity="secondary" />
                        <Menu :popup="true" />
                    </template>
                </Column>
            </DataTable>
            <Paginator :rows="10" :totalRecords="totalItems" v-model:first="currentItems" @update:first="changePage()">
                <template #start="slotProps"> {{ $tt('patientRequest.table.total') }} {{ totalItems }}</template>
                <template #end></template>
            </Paginator>
        </div>
        <create-reception ref="createRef" @callFetchData="fetchPatientRequest" />
    </div>
</template>

<style lang="scss" scoped>
:deep(.p-datatable-frozen-tbody) {
    font-weight: bold;
}

:deep(.p-datatable-scrollable .p-frozen-column) {
    font-weight: bold;
}
</style>
