<script setup>
import DeathNumberService from '@/service/DeathNumberService';
import { inject, onBeforeMount, ref } from 'vue';
import SETTINGS from '@/constants/settings';
import Create from '@/views/deathnumber/Create.vue';
import Edit from '@/views/deathnumber/Edit.vue';
import ConfirmDelete from '@/views/deathnumber/ConfirmDelete.vue';
import Common from '@/constants/common';

const deathNumberService = DeathNumberService.INSTANCE;

const keyword = ref('');
const pageNumber = ref(0);
const totalPages = ref();
const totalItems = ref();
const currentItems = ref();
const statuses = ref([Common.STATUS.ACTIVATED]);
const deathNumbers = ref();
const $loading = inject('$loading');

async function fetchDeathNumbers() {
    let params = {
        keyword: keyword.value,
        status: statuses.value,
        pageNo: pageNumber.value
    };
    const loader = $loading.show(SETTINGS.LOADING_PROPERTIES);
    try {
        const res = await deathNumberService.getDeathNumbers(params);
        deathNumbers.value = res.payload.data;
        pageNumber.value = res.payload.currentPage;
        totalPages.value = res.payload.totalPages;
        totalItems.value = res.payload.totalItems;
    } finally {
        setTimeout(() => loader.hide(), 500);
    }
}

onBeforeMount(async () => {
    await fetchDeathNumbers();
});

function truncateString(str, maxLength = 45) {
    if (str.length <= maxLength) {
        return str;
    }
    return str.substring(0, maxLength) + '...';
}

const changePage = async () => {
    pageNumber.value = currentItems.value / 10 + 1;
    await fetchDeathNumbers();
};

const createRef = ref();

function onClickNew() {
    createRef.value.visible = true;
}

const editRef = ref();
const dataEdit = ref();

function onClickEdit(record) {
    editRef.value.visible = true;
    dataEdit.value = record;
}

const deleteRef = ref();

function onClickDelete(id) {
    deleteRef.value.idDeathNumber = id;
    deleteRef.value.confirmDelete();
}
</script>

<template>
    <div>
        <div class="card">
            <Toolbar class="mb-2">
                <template v-slot:start>
                    <Button type="button" :label="$tt('death-number-manager.button.add')" icon="pi pi-pen-to-square" @click="onClickNew()" />
                </template>
                <template v-slot:end>
                    <FloatLabel class="mr-4">
                        <InputText id="keyword" v-model="keyword" />
                        <label for="keyword">{{ $tt('death-number-manager.input.search') }}</label>
                    </FloatLabel>
                    <Button type="button" :label="$tt('death-number-manager.button.search')" icon="pi pi-search" severity="success" @click="fetchDeathNumbers" />
                </template>
            </Toolbar>
            <DataTable :value="deathNumbers" scrollable scrollHeight="500px" tableStyle="min-width: 40rem">
                <template #header>
                    <div class="flex flex-wrap items-center justify-between gap-2">
                        <span class="text-xl font-bold">{{ $tt('death-number-manager.table.title') }}</span>
                    </div>
                </template>
                <Column field="deathNumber" :header="$tt('death-number-manager.table.order')" bodyClass="text-center" headerStyle="font-weight:bold" style="min-width: 40px"></Column>
                <Column field="patientName" :header="$tt('death-number-manager.table.name')" style="min-width: 150px"></Column>
                <Column field="department" :header="$tt('death-number-manager.table.department')" style="min-width: 70px"></Column>
                <Column field="sex" :header="$tt('death-number-manager.table.sex')" style="min-width: 50px"></Column>
                <Column field="birthday" :header="$tt('death-number-manager.table.birthday')" style="min-width: 90px"></Column>
                <Column field="deathDate" :header="$tt('death-number-manager.table.death-date')" style="min-width: 80px"></Column>
                <Column :header="$tt('death-number-manager.table.diagnosis-of-death')" bodyClass="text-left" headerStyle="font-weight: bold" style="min-width: 150px">
                    <template #body="{ data }">
                        {{ truncateString(data.diagnosisOfDeath) }}
                    </template>
                </Column>
                <Column field="submitter" :header="$tt('death-number-manager.table.submitter')" style="min-width: 50px"></Column>
                <Column field="submitterRecord" :header="$tt('death-number-manager.table.submitter-record')" style="min-width: 50px"></Column>
                <Column :header="$tt('death-number-manager.table.actions')" style="min-width: 50px">
                    <template #body="{ data }">
                        <div class="flex flex-row">
                            <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="onClickEdit(data)" />
                            <Button icon="pi pi-trash" outlined rounded severity="danger" @click="onClickDelete(data.id)" />
                        </div>
                    </template>
                </Column>
            </DataTable>
            <Paginator :rows="10" :totalRecords="totalItems" v-model:first="currentItems" @update:first="changePage()">
                <template #start="slotProps"> {{ $tt('death-number-manager.table.total') }} {{ totalItems }}</template>
                <template #end></template>
            </Paginator>
        </div>
        <create ref="createRef" @callFetchDeathNumbers="fetchDeathNumbers()"></create>
        <edit ref="editRef" :dataEdit="dataEdit" @callFetchDeathNumbers="fetchDeathNumbers()" />
        <confirm-delete ref="deleteRef" @callFetchDeathNumbers="fetchDeathNumbers()" />
    </div>
</template>

<style scoped lang="scss"></style>
