<script setup>
import { onBeforeMount, ref, watch } from 'vue';
import { useCommonStore } from '@/store/commonStore';
import { storeToRefs } from 'pinia';

const commonStore = useCommonStore();
const { departments } = storeToRefs(commonStore);
onBeforeMount(async () => {
    await commonStore.loadDepartments();
});

const visible = ref(false);
const patientData = ref([]);
defineExpose({ patientData, visible });

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

watch(patientData, (newValue) => {
    newValue.inDate = formatDate(newValue.inDate);
    newValue.outDate = formatDate(newValue.outDate);
    newValue.appointmentDate = formatDate(newValue.appointmentDate);
});

async function copyClick() {
    if (patientData.value.patientNumber) {
    }
}
</script>

<template>
    <Dialog v-model:visible="visible" modal :header="$tt('edit-copy-record.header')" :style="{ width: '50rem' }">
        <Fluid>
            <span class="text-surface-500 dark:text-surface-400 block mb-4">{{ $tt('edit-copy-record.title') }}</span>
            <div class="flex flex-col md:flex-row">
                <div class="grid grid-cols-12 gap-8">
                    <div class="flex flex-wrap col-span-3 gap-2 w-full">
                        <label for="numberOrder">{{ $tt('edit-copy-record.label.number-order') }}</label>
                        <InputNumber disabled id="numberOrder" :useGrouping="false" v-model="patientData.numberOrder" />
                    </div>
                    <div class="flex flex-wrap col-span-4 gap-2 w-full">
                        <label for="department">{{ $tt('edit-copy-record.label.department') }}</label>
                        <Dropdown id="department" @change="calReturnDate" v-model="patientData.department" :options="departments" editable option-value="name" optionLabel="name"></Dropdown>
                    </div>
                    <div class="flex flex-wrap col-span-5 gap-2 w-full">
                        <label for="patientName">{{ $tt('edit-copy-record.label.patient-name') }}</label>
                        <InputText id="patientName" v-model="patientData.patientName" />
                    </div>
                </div>
            </div>

            <div class="flex flex-col md:flex-row gap-8 mt-6">
                <div class="grid grid-cols-12 gap-8">
                    <div class="flex flex-wrap col-span-3 gap-2 w-full">
                        <label for="appointmentCopyDate">{{ $tt('edit-copy-record.label.appointment-date') }}</label>
                        <DatePicker disabled id="appointmentCopyDate" dateFormat="dd/mm/yy" showIcon v-model="patientData.appointmentDate" />
                    </div>
                    <div class="flex flex-wrap col-span-3 gap-2 w-full">
                        <label for="numberOrder">{{ $tt('edit-copy-record.label.patient-number') }}</label>
                        <InputNumber id="patientNumber" :useGrouping="false" v-model="patientData.patientNumber" />
                    </div>
                    <div class="flex flex-wrap col-span-3 gap-2 w-full">
                        <label for="inDate">{{ $tt('edit-copy-record.label.in-date') }}</label>
                        <DatePicker id="inDate" showIcon dateFormat="dd/mm/yy" v-model="patientData.inDate" />
                    </div>
                    <div class="flex flex-wrap col-span-3 gap-2 w-full">
                        <label for="outDate">{{ $tt('edit-copy-record.label.out-date') }}</label>
                        <DatePicker id="outDate" dateFormat="dd/mm/yy" showIcon v-model="patientData.outDate" />
                    </div>
                </div>
            </div>

            <div class="flex flex-col justify-end md:flex-row mt-6 gap-8">
                <div class="flex flex-row gap-2">
                    <Button type="button" icon="pi pi-times-circle" :label="$tt('edit-copy-record.button.cancel')" severity="secondary" @click="visible = false"></Button>
                    <Button type="button" icon="pi pi-file-word" :label="$tt('edit-copy-record.button.save')" @click="copyClick"></Button>
                </div>
            </div>
        </Fluid>
    </Dialog>
</template>

<style scoped lang="scss"></style>
