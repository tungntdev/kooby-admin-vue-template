<script setup>
import { defineEmits, inject, ref } from 'vue';
import PatientRequestService from '@/service/PatientRequestService';
import FormComponent from '@/views/reception/FormComponent.vue';
import SETTINGS from '@/constants/settings';

const visible = ref(false);
const props = defineProps({
    dataEdit: []
});
const formComponentRef = ref();
defineExpose({ visible, formComponentRef });

const $loading = inject('$loading');
const patientRequestService = PatientRequestService.INSTANCE;

const emit = defineEmits(['callFetchData']);

async function onClickUpdate() {
    if (formComponentRef.value.checkValidation()) {
        const loader = $loading.show(SETTINGS.LOADING_PROPERTIES);
        try {
            formComponentRef.value.dataInput.inDate = new Date(formComponentRef.value.dataInput.inDate);
            formComponentRef.value.dataInput.outDate = new Date(formComponentRef.value.dataInput.outDate);
            formComponentRef.value.dataInput.receptionDate = new Date(formComponentRef.value.dataInput.receptionDate);
            formComponentRef.value.dataInput.appointmentPatientDate = new Date(formComponentRef.value.dataInput.appointmentPatientDate);

            console.debug(formComponentRef.value.dataInput);

            await patientRequestService.update(formComponentRef.value.dataInput);
        } finally {
            setTimeout(() => loader.hide(), 500);
            visible.value = false;
            await emit('callFetchData');
        }
    }
}

function convertToUTCPlus7(date) {
    const utcDate = new Date(date);
    utcDate.setHours(utcDate.getHours() + 7);
    return utcDate;
}
</script>

<template>
    <Dialog v-model:visible="visible" :style="{ width: '60vw' }" :header="$tt('patient_reception_component.title.update')" modal>
        <Fluid>
            <div class="card flex flex-col gap-6 w-full">
                <form-component ref="formComponentRef" :dataFormEdit="props.dataEdit"></form-component>
                <div class="flex flex-col justify-end md:flex-row gap-8">
                    <div class="flex flex-row gap-2">
                        <Button type="button" :label="$tt('patient_reception_component.button.cancel')" severity="secondary" @click="visible = false"></Button>
                        <Button type="button" :label="$tt('patient_reception_component.button.update')" @click="onClickUpdate"></Button>
                    </div>
                </div>
            </div>
        </Fluid>
    </Dialog>
</template>

<style scoped lang="scss"></style>
