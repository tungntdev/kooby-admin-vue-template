<script setup>
import StorageService from '@/service/StorageService';
import { inject, onBeforeMount, ref } from 'vue';
import Common from '@/constants/common';
import SETTINGS from '@/constants/settings';
import ConfirmDelete from '@/views/storage/ConfirmDelete.vue';
import Create from '@/views/storage/Create.vue';
import Edit from '@/views/storage/Edit.vue';

const storageService = StorageService.INSTANCE;

const keyword = ref('');
const pageNumber = ref(1);
const totalPages = ref();
const totalItems = ref();
const currentItems = ref();
const statuses = ref([Common.STATUS.ACTIVATED]);
const storages = ref();
const $loading = inject('$loading');
const selectedDepartment = ref('');
const departments = ref([
    { name: 'Tất cả', key: '' },
    { name: 'A6-A', key: 'A6-A' },
    { name: 'A6-B', key: 'A6-B' },
    { name: 'A6-D', key: 'A6-D' },
    { name: 'A18', key: 'A18' },
    { name: 'A6-C', key: 'A6-C' },
    { name: 'A15', key: 'A15' }
]);

async function fetchData() {
    let params = {
        keyword: keyword.value,
        department: selectedDepartment.value,
        status: statuses.value,
        pageNo: pageNumber.value
    };
    const loader = $loading.show(SETTINGS.LOADING_PROPERTIES);
    try {
        const res = await storageService.search(params);
        storages.value = res.payload.data;
        pageNumber.value = res.payload.currentPage;
        totalPages.value = res.payload.totalPages;
        totalItems.value = res.payload.totalItems;
    } finally {
        setTimeout(() => loader.hide(), 500);
    }
}

onBeforeMount(async () => {
    await fetchData();
});

function truncateString(str, maxLength = 35) {
    try {
        if (str.length <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + '...';
    } catch (err) {
        return null;
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

const changePage = async () => {
    pageNumber.value = currentItems.value / 10 + 1;
    await fetchData();
};

async function onClickChangeDepartment(department) {
    selectedDepartment.value = department;
    await fetchData();
}

const deleteRef = ref();

function onClickDelete(id) {
    console.debug('Click xoa');
    deleteRef.value.idStorage = id;
    deleteRef.value.confirmDelete();
}

const createRef = ref();

async function onClickNew() {
    createRef.value.visible = true;
}

const editRef = ref();
const dataEdit = ref();

function onClickEdit(record) {
    editRef.value.visible = true;
    dataEdit.value = record;
    console.debug(record);
}
</script>

<template>
    <div>
        <div class="card">
            <Toolbar class="mb-2">
                <template v-slot:start>
                    <Button type="button" :label="$tt('storage_manager.button.add')" icon="pi pi-pen-to-square" @click="onClickNew()" />
                </template>

                <template v-slot:center>
                    <div class="flex flex-wrap gap-3">
                        <div v-for="department in departments" :key="department.key" class="flex align-items-center">
                            <RadioButton v-model="selectedDepartment" :inputId="department.key" :value="department.key" @click="onClickChangeDepartment(department.key)" />
                            <label :for="department.key" class="ml-2">{{ department.name }}</label>
                        </div>
                    </div>
                </template>

                <template v-slot:end>
                    <FloatLabel class="mr-4">
                        <InputText id="keyword" v-model="keyword" @keydown.enter="fetchData" />
                        <label for="keyword">{{ $tt('storage_manager.input.search') }}</label>
                    </FloatLabel>
                    <Button type="button" :label="$tt('storage_manager.button.search')" icon="pi pi-search" severity="success" @click="fetchData" />
                </template>
            </Toolbar>
            <DataTable :value="storages" scrollable scrollHeight="580px" tableStyle="min-width: 40rem">
                <template #header>
                    <div class="flex flex-wrap items-center justify-between gap-2">
                        <span class="text-xl font-bold">{{ $tt('storage_manager.table.title') }}</span>
                    </div>
                </template>
                <Column field="department" :header="$tt('storage_manager.table.department')" style="min-width: 70px"></Column>
                <Column field="saveNumber" :header="$tt('storage_manager.table.save-number')" bodyClass="text-center" headerStyle="font-weight:bold" style="min-width: 50px"></Column>
                <Column field="saveYear" :header="$tt('storage_manager.table.save-year')" bodyClass="text-center" headerStyle="font-weight:bold" style="min-width: 50px"></Column>
                <Column field="patientName" :header="$tt('storage_manager.table.name')" style="min-width: 150px"></Column>

                <Column field="sex" :header="$tt('storage_manager.table.sex')" style="min-width: 50px"></Column>
                <Column field="birthday" :header="$tt('storage_manager.table.birthday')" style="min-width: 90px"></Column>
                <Column :header="$tt('storage_manager.table.in_date')" bodyClass="text-left" headerStyle="font-weight: bold" style="min-width: 80px">
                    <template #body="{ data }">
                        {{ formatDate(data.inDate) }}
                    </template>
                </Column>
                <Column :header="$tt('storage_manager.table.out_date')" bodyClass="text-left" headerStyle="font-weight: bold" style="min-width: 80px">
                    <template #body="{ data }">
                        {{ formatDate(data.outDate) }}
                    </template>
                </Column>
                <Column :header="$tt('storage_manager.table.diagnose')" bodyClass="text-left" headerStyle="font-weight: bold" style="min-width: 150px">
                    <template #body="{ data }">
                        {{ truncateString(data.diagnose) }}
                    </template>
                </Column>

                <Column :header="$tt('storage_manager.table.actions')" style="min-width: 50px">
                    <template #body="{ data }">
                        <div class="flex flex-row">
                            <Button icon="pi pi-pencil" outlined rounded severity="secondary" class="mr-2" @click="onClickEdit(data)" />
                            <Button icon="pi pi-trash" outlined rounded severity="danger" @click="onClickDelete(data.id)" />
                        </div>
                    </template>
                </Column>
            </DataTable>
            <Paginator :rows="Common.DEFAULT_ROW_PAGE" :totalRecords="totalItems" v-model:first="currentItems" @update:first="changePage()">
                <template #start="slotProps"> {{ $tt('storage_manager.table.total') }} {{ totalItems }}</template>
                <template #end></template>
            </Paginator>
        </div>
        <confirm-delete ref="deleteRef" @callFetchStorages="fetchData"></confirm-delete>
        <create ref="createRef" @callFetchStorages="fetchData" />
        <edit ref="editRef" :dataEdit="dataEdit" @callFetchStorages="fetchData" />
    </div>
</template>

<style scoped lang="scss"></style>
