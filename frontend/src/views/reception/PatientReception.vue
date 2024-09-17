<script setup>
import { inject, onBeforeMount, ref } from 'vue';
import PatientRequestService from '@/service/PatientRequestService';
import Common from '@/constants/common';
import SETTINGS from '@/constants/settings';
import CreateReception from '@/views/reception/CreateReception.vue';
import { translate } from '@/locales';
import EditReception from '@/views/reception/EditReception.vue';

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
            return translate('patient-request.table.delivery-sate');
        }
    }
};

const createRef = ref();
const nextNumberOrder = ref(0);

async function onClickNew() {
    createRef.value.visible = true;
}

const menu = ref();
const selectedPatient = ref();
const items = ref([]);

const toggle = (data) => {
    menu.value.toggle(event);
    selectedPatient.value = data;
    items.value = [
        {
            label: translate('patient-request.menu.options'),
            items: [
                {
                    label: translate('patient-request.menu.edit'),
                    icon: 'pi pi-file-edit',
                    command: async () => await edit()
                },
                {
                    label: translate('patient-request.menu.assignment'),
                    icon: 'pi pi-user'
                },
                {
                    label: translate('patient-request.menu.signed'),
                    icon: 'pi pi-pencil'
                },
                {
                    label: translate('patient-request.menu.delivered'),
                    icon: 'pi pi-send'
                },
                {
                    label: translate('patient-request.menu.received'),
                    icon: 'pi pi-verified'
                },
                {
                    label: translate('patient-request.menu.print-ticket'),
                    icon: 'pi pi-print'
                },
                {
                    label: translate('patient-request.menu.delete'),
                    icon: 'pi pi-trash'
                }
            ]
        }
    ];
};
const editRef = ref();
const dataEdit = ref();

async function edit() {
    editRef.value.visible = true;
    dataEdit.value = selectedPatient.value;
}
</script>

<template>
    <div>
        <div class="card">
            <Toolbar class="mb-2">
                <template v-slot:start>
                    <Button type="button" :label="$tt('patient-request.button.add')" icon="pi pi-pen-to-square" @click="onClickNew()" />
                </template>
                <template v-slot:end>
                    <Button type="button" :label="$tt('patient-request.button.search')" icon="pi pi-search" severity="success" />
                </template>
            </Toolbar>
            <DataTable :value="patientRequests" size="small" scrollable scrollHeight="500px" tableStyle="min-width: 50rem">
                <template #header>
                    <div class="flex flex-wrap items-center justify-between gap-2">
                        <span class="text-xl font-bold">{{ $tt('patient-request.table.title') }}</span>
                    </div>
                </template>
                <Column field="numberOrder" :header="$tt('patient-request.table.order')" bodyClass="text-center" headerStyle="font-weight:bold" style="min-width: 50px"></Column>
                <Column field="patientName" frozen :header="$tt('patient-request.table.name')" style="min-width: 150px"></Column>
                <Column field="department" :header="$tt('patient-request.table.department')" style="min-width: 50px"></Column>
                <Column :header="$tt('patient-request.table.inDate')" bodyClass="text-left" headerStyle="font-weight: bold" style="min-width: 80px">
                    <template #body="{ data }">
                        {{ formatDate(data.inDate) }}
                    </template>
                </Column>
                <Column :header="$tt('patient-request.table.outDate')" style="min-width: 80px">
                    <template #body="{ data }">
                        {{ formatDate(data.outDate) }}
                    </template>
                </Column>
                <Column field="copyQuantity" :header="$tt('patient-request.table.quantity')" style="min-width: 50px"></Column>
                <Column field="note" :header="$tt('patient-request.table.note')" style="min-width: 100px"></Column>
                <Column field="stateName" :header="$tt('patient-request.table.state')" style="min-width: 80px">
                    <template #body="{ data }">
                        <Tag :value="data.stateName" :severity="getSeverity(data.state)" />
                    </template>
                </Column>

                <Column field="stateName" :header="$tt('patient-request.table.delivery')" style="min-width: 80px">
                    <template #body="{ data }">
                        <div v-if="data.deliveryOrderNumber">
                            <Tag :value="getDeliveryState(data)" severity="danger" />
                        </div>
                    </template>
                </Column>
                <Column field="firstName" :header="$tt('patient-request.table.copy')" style="min-width: 80px"></Column>
                <Column :header="$tt('patient-request.table.signDate')" style="min-width: 80px">
                    <template #body="{ data }">
                        {{ formatDate(data.signDate) }}
                    </template>
                </Column>
                <Column :header="$tt('patient-request.table.done')" style="min-width: 80px">
                    <template #body="{ data }">
                        {{ formatDate(data.donePatientDate) }}
                    </template>
                </Column>
                <Column :header="$tt('patient-request.table.actions')" frozen alignFrozen="right" style="min-width: 50px">
                    <template #body="{ data }">
                        <Button aria-label="Actions" @click="toggle(data)" icon="pi pi-spin pi-cog" rounded severity="secondary" />
                        <Menu ref="menu" id="overlay_menu" :model="items" :popup="true" />
                    </template>
                </Column>
            </DataTable>
            <Paginator :rows="10" :totalRecords="totalItems" v-model:first="currentItems" @update:first="changePage()">
                <template #start="slotProps"> {{ $tt('patient-request.table.total') }} {{ totalItems }}</template>
                <template #end></template>
            </Paginator>
        </div>
        <create-reception ref="createRef" @callFetchData="fetchPatientRequest" />
        <edit-reception ref="editRef" :dataEdit="dataEdit" @callFetchData="fetchPatientRequest" />
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
