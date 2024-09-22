<script setup>
import { defineEmits, inject, onBeforeMount, ref } from 'vue';
import { useCommonStore } from '@/store/commonStore';
import { storeToRefs } from 'pinia';
import PatientRequestService from '@/service/PatientRequestService';
import SETTINGS from '@/constants/settings';

const visible = ref(false);
const patientData = ref([]);
defineExpose({ patientData, visible });
const minDate = ref(new Date());

const appointmentCopyDate = ref();
const copier = ref();
const commonStore = useCommonStore();
const { listCopyUsers } = storeToRefs(commonStore);
onBeforeMount(async () => {
    await commonStore.loadCopyUsers();
});

const patientRequestService = PatientRequestService.INSTANCE;
const $loading = inject('$loading');
const emit = defineEmits(['callFetchData']);

async function setAssignmentClick() {
    const loader = $loading.show(SETTINGS.LOADING_PROPERTIES);
    const data = {
        id: patientData.value.id,
        idCopyUser: copier.value,
        appointmentDate: appointmentCopyDate.value
    };
    try {
        await patientRequestService.setAssignment(data);
    } finally {
        setTimeout(() => loader.hide(), 500);
        visible.value = false;
        emit('callFetchData');
    }
}
</script>

<template>
    <Dialog v-model:visible="visible" modal :header="$tt('patient_reception_assignment.header')" :style="{ width: '60rem' }">
        <Fluid>
            <span class="text-surface-500 dark:text-surface-400 block mb-8">{{ $tt('patient_reception_assignment.span') }}</span>
            <div class="flex flex-col md:flex-row">
                <div class="grid grid-cols-12 gap-8">
                    <div class="flex flex-wrap col-span-2 gap-2 w-full">
                        <label for="numberOrder">{{ $tt('patient_reception_assignment.label.number-order') }}</label>
                        <InputNumber disabled id="numberOrder" :useGrouping="false" v-model="patientData.numberOrder" />
                    </div>
                    <div class="flex flex-wrap col-span-3 gap-2 w-full">
                        <label for="medicineCode">{{ $tt('patient_reception_assignment.label.medicine-code') }}</label>
                        <InputMask disabled id="medicineCode" mask="99/999999" placeholder="24/xxxxxx" v-model="patientData.medicineCode" />
                    </div>
                    <div class="flex flex-wrap col-span-4 gap-2 w-full">
                        <label for="patientName">{{ $tt('patient_reception_assignment.label.patient-name') }}</label>
                        <InputText disabled id="patientName" v-model="patientData.patientName" />
                    </div>
                    <div class="flex flex-wrap col-span-3 gap-2 w-full">
                        <label for="department">{{ $tt('patient_reception_assignment.label.department') }}</label>
                        <InputText disabled id="department" v-model="patientData.department" />
                    </div>
                </div>
            </div>

            <div class="flex flex-col md:flex-row gap-8 mt-6">
                <div class="grid grid-cols-12 gap-8">
                    <div class="flex flex-wrap col-span-3 gap-2 w-full">
                        <label for="receptionDate">{{ $tt('patient_reception_assignment.label.reception-date') }}</label>
                        <DatePicker disabled id="receptionDate" showIcon dateFormat="dd/mm/yy" v-model="patientData.receptionDate" />
                    </div>
                    <div class="flex flex-wrap col-span-3 gap-2 w-full">
                        <label for="appointmentPatientDate">{{ $tt('patient_reception_assignment.label.appointment-patient-date') }}</label>
                        <DatePicker disabled id="appointmentPatientDate" dateFormat="dd/mm/yy" showIcon v-model="patientData.appointmentPatientDate" />
                    </div>
                    <div class="flex flex-wrap col-span-3 gap-2 w-full">
                        <label for="appointmentCopyDate">{{ $tt('patient_reception_assignment.label.appointment-copy-date') }}</label>
                        <DatePicker id="appointmentCopyDate" :minDate="minDate" :maxDate="maxDate" dateFormat="dd/mm/yy" showIcon v-model="appointmentCopyDate" />
                    </div>
                    <div class="flex flex-wrap col-span-3 gap-2 w-full">
                        <label for="copier">{{ $tt('patient_reception_assignment.label.copier') }}</label>
                        <Dropdown id="copier" v-model="copier" :options="listCopyUsers" editable option-value="id" optionLabel="firstName"></Dropdown>
                    </div>
                </div>
            </div>

            <div class="flex flex-col justify-end md:flex-row mt-6 gap-8">
                <div class="flex flex-row gap-2">
                    <Button type="button" icon="pi pi-times-circle" :label="$tt('patient_reception_assignment.button.cancel')" severity="secondary" @click="visible = false"></Button>
                    <Button type="button" icon="pi pi-save" :label="$tt('patient_reception_assignment.button.save')" @click="setAssignmentClick"></Button>
                </div>
            </div>
        </Fluid>
    </Dialog>
</template>

<style scoped lang="scss"></style>
