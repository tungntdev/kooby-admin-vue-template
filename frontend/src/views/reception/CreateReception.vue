<script setup>
import FormComponent from '@/views/reception/FormComponent.vue';
import { defineEmits, inject, ref } from 'vue';
import PatientRequestService from '@/service/PatientRequestService';
import SETTINGS from '@/constants/settings';

const visible = ref(false);
defineExpose({ visible });
const formComponentRef = ref();

const emit = defineEmits(['callFetchData']);
const $loading = inject('$loading');

const patientRequestService = PatientRequestService.INSTANCE;

async function onClickSave() {
    if (formComponentRef.value.checkValidation()) {
        const loader = $loading.show(SETTINGS.LOADING_PROPERTIES);
        try {
            console.debug(formComponentRef.value.dataInput);
            await patientRequestService.create(formComponentRef.value.dataInput);
        } finally {
            setTimeout(() => loader.hide(), 500);
            visible.value = false;
            emit('callFetchData');
        }
    } else {
        console.debug('Không validation');
    }
}
</script>

<template>
    <Dialog v-model:visible="visible" :style="{ width: '60vw' }" :header="$tt('patient_reception_component.title.create')" modal>
        <Fluid>
            <div class="card flex flex-col gap-6 w-full">
                <form-component ref="formComponentRef"></form-component>
                <div class="flex flex-col justify-end md:flex-row gap-8">
                    <div class="flex flex-row gap-2">
                        <Button type="button" icon="pi pi-times-circle" :label="$tt('patient_reception_component.button.cancel')" severity="secondary" @click="visible = false"></Button>
                        <Button type="button" icon="pi pi-save" :label="$tt('patient_reception_component.button.save')" @click="onClickSave"></Button>
                    </div>
                </div>
            </div>
        </Fluid>
    </Dialog>
</template>

<style scoped lang="scss"></style>
