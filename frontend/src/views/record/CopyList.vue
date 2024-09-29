<script setup>
import { inject, onBeforeMount, ref } from 'vue';
import PatientRequestService from '@/service/PatientRequestService';
import Common from '@/constants/common';
import SETTINGS from '@/constants/settings';
import { translate } from '@/locales';
import CopyRecord from '@/views/record/CopyRecord.vue';
import EditPatientNumber from '@/views/record/EditPatientNumber.vue';
import ConfirmCopied from '@/views/record/ConfirmCopied.vue';

const patientRequestService = PatientRequestService.INSTANCE;

const keyword = ref('');
const pageNumber = ref(0);
const totalPages = ref();
const totalItems = ref();
const currentItems = ref();
const states = ref([Common.STATES.ASSIGNED]);
const patientRequests = ref();

const selectedState = ref('1');
const listStates = ref([
    { name: 'Tất cả', key: '' },
    { name: 'Mới phân công', key: '1' },
    { name: 'Đang sao', key: '2' },
    { name: 'Đã ký', key: '4' },
    { name: 'Hoàn thành', key: '6' }
]);

async function onChangeStates() {
    states.value = [];
    if (selectedState.value) {
        states.value.push(selectedState.value);
    }

    await fetchPatientRequest();
}

const $loading = inject('$loading');

async function fetchPatientRequest() {
    let params = {
        keyword: keyword.value,
        states: states.value,
        pageNo: pageNumber.value,
        copyUser: 1
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
        if (data.delivery === 1) {
            return translate('copy-list.table.delivery-sate');
        }
    }
};

const menu = ref();
const selectedPatient = ref();
const items = ref([]);

const toggle = (data) => {
    menu.value.toggle(event);
    selectedPatient.value = data;
    const editMenu = {
        label: translate('copy-list.menu.edit'),
        icon: 'pi pi-file-edit',
        command: async () => await editClick()
    };

    const copyMenu = {
        label: translate('copy-list.menu.copy'),
        icon: 'pi pi-pen-to-square',
        command: async () => await copyClick()
    };

    const confirmMenu = {
        label: translate('copy-list.menu.confirm'),
        icon: 'pi pi-check-circle',
        command: async () => await confirmClick()
    };

    const borrowMenu = {
        label: translate('copy-list.menu.borrow'),
        icon: 'pi pi-send'
    };

    switch (data.state) {
        case Common.STATES.PENDING:
            items.value = [
                {
                    label: translate('copy-list.menu.options'),
                    items: [editMenu, copyMenu, confirmMenu, borrowMenu]
                }
            ];
            break;

        case Common.STATES.ASSIGNED:
            items.value = [
                {
                    label: translate('copy-list.menu.options'),
                    items: [editMenu, copyMenu, confirmMenu, borrowMenu]
                }
            ];
            break;

        case Common.STATES.IN_PROGRESS:
            items.value = [
                {
                    label: translate('copy-list.menu.options'),
                    items: [copyMenu, borrowMenu]
                }
            ];
            break;

        case Common.STATES.DIRECTOR_APPROVED:
            items.value = [
                {
                    label: translate('copy-list.menu.options'),
                    items: [copyMenu, borrowMenu]
                }
            ];
            break;

        case Common.STATES.COMPLETE:
            items.value = [
                {
                    label: translate('copy-list.menu.options'),
                    items: [copyMenu]
                }
            ];
            break;
    }
};
const confirmRef = ref();

async function confirmClick() {
    await confirmRef.value.confirmClick();
    confirmRef.value.patient = selectedPatient.value;
    confirmRef.value.confirmType = Common.CONFIRM_TYPE.COPIED;
}

const copyRecordRef = ref();

async function copyClick() {
    copyRecordRef.value.visible = true;
    copyRecordRef.value.patientData = selectedPatient.value;
}

const editPatientNumberRef = ref();

async function editClick() {
    editPatientNumberRef.value.visible = true;
    editPatientNumberRef.value.patientData = selectedPatient.value;
}

function getLinkAvatar(userLink) {
    if (userLink) {
        return '/avatars/' + userLink + '-min.jpg';
    } else {
        return '/avatars/no-user-min.jpg';
    }
}
</script>

<template>
    <div>
        <div class="card">
            <Toolbar class="mb-2">
                <template v-slot:center>
                    <div class="flex flex-wrap gap-3">
                        <div v-for="state in listStates" :key="state.key" class="flex align-items-center">
                            <RadioButton v-model="selectedState" :inputId="state.key" :value="state.key" @change="onChangeStates" />
                            <label :for="state.key" class="ml-2">{{ state.name }}</label>
                        </div>
                    </div>
                </template>

                <template v-slot:end>
                    <FloatLabel class="mr-4">
                        <InputText id="keyword" v-model="keyword" @keydown.enter="fetchPatientRequest" />
                        <label for="keyword">{{ $tt('storage_manager.input.search') }}</label>
                    </FloatLabel>
                    <Button type="button" :label="$tt('copy-list.button.search')" icon="pi pi-search" severity="success" @click="fetchPatientRequest" />
                </template>
            </Toolbar>
            <DataTable :value="patientRequests" scrollable scrollHeight="600px" tableStyle="min-width: 50rem">
                <template #header>
                    <div class="flex flex-wrap items-center justify-between gap-2">
                        <span class="text-xl font-bold">{{ $tt('copy-list.table.title') }}</span>
                    </div>
                </template>
                <Column field="numberOrder" :header="$tt('copy-list.table.order')" bodyClass="text-center" headerStyle="font-weight:bold" style="min-width: 50px"></Column>
                <Column field="patientName" frozen :header="$tt('copy-list.table.name')" style="min-width: 150px"></Column>
                <Column field="department" :header="$tt('copy-list.table.department')" style="min-width: 50px"></Column>
                <Column :header="$tt('copy-list.table.inDate')" bodyClass="text-left" headerStyle="font-weight: bold" style="min-width: 80px">
                    <template #body="{ data }">
                        {{ formatDate(data.inDate) }}
                    </template>
                </Column>
                <Column :header="$tt('copy-list.table.outDate')" style="min-width: 80px">
                    <template #body="{ data }">
                        {{ formatDate(data.outDate) }}
                    </template>
                </Column>
                <Column :header="$tt('copy-list.table.appointmentPatientDate')" style="min-width: 80px">
                    <template #body="{ data }">
                        {{ formatDate(data.appointmentPatientDate) }}
                    </template>
                </Column>
                <Column field="copyQuantity" :header="$tt('copy-list.table.quantity')" style="min-width: 50px"></Column>
                <Column field="note" :header="$tt('copy-list.table.note')" style="min-width: 100px"></Column>
                <Column :header="$tt('copy-list.table.state')" style="min-width: 80px">
                    <template #body="{ data }">
                        <Tag :value="data.stateName" :severity="getSeverity(data.state)" />
                    </template>
                </Column>

                <Column :header="$tt('copy-list.table.delivery')" style="min-width: 80px">
                    <template #body="{ data }">
                        <div v-if="data.delivery">
                            <Tag :value="getDeliveryState(data)" severity="danger" />
                        </div>
                    </template>
                </Column>

                <Column :header="$tt('copy-list.table.copy')" style="min-width: 80px">
                    <template #body="{ data }">
                        <Avatar v-tooltip="{ value: data.firstName, showDelay: 300, hideDelay: 1000 }" :image="getLinkAvatar(data.imageLink)" shape="circle" />
                    </template>
                </Column>

                <Column :header="$tt('copy-list.table.signDate')" style="min-width: 80px">
                    <template #body="{ data }">
                        {{ formatDate(data.signDate) }}
                    </template>
                </Column>
                <Column :header="$tt('copy-list.table.done')" style="min-width: 80px">
                    <template #body="{ data }">
                        {{ formatDate(data.donePatientDate) }}
                    </template>
                </Column>
                <Column :header="$tt('copy-list.table.actions')" frozen alignFrozen="right" style="min-width: 50px">
                    <template #body="{ data }">
                        <Button aria-label="Actions" @click="toggle(data)" icon="pi pi-spin pi-cog" rounded severity="secondary" />
                        <Menu ref="menu" id="overlay_menu" :model="items" :popup="true" />
                    </template>
                </Column>
            </DataTable>
            <Paginator :rows="10" :totalRecords="totalItems" v-model:first="currentItems" @update:first="changePage()">
                <template #start="slotProps"> {{ $tt('copy-list.table.total') }} {{ totalItems }}</template>
                <template #end></template>
            </Paginator>
        </div>
        <copy-record ref="copyRecordRef" />
        <edit-patient-number ref="editPatientNumberRef" />
        <confirm-copied ref="confirmRef" @callFetchData="fetchPatientRequest" />
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
