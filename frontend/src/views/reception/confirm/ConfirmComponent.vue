<script setup>
import { useToast } from 'primevue/usetoast';
import { useConfirm } from 'primevue/useconfirm';
import { defineEmits, inject, ref, watch } from 'vue';
import SETTINGS from '@/constants/settings';
import { translate } from '@/locales';
import PatientRequestService from '@/service/PatientRequestService';
import Common from '@/constants/common';

const toast = useToast();
const confirm = useConfirm();
const patient = ref([]);
const confirmType = ref();

const $loading = inject('$loading');
const patientRequestService = PatientRequestService.INSTANCE;
const header = ref();
const message = ref();

watch(confirmType, (newValue) => {
    switch (newValue) {
        case Common.CONFIRM_TYPE.DELETE:
            header.value = translate('patient-reception_confirm.delete.label.header');
            message.value = translate('patient-reception_confirm.delete.label.message');
            break;
        case Common.CONFIRM_TYPE.SIGNED:
            header.value = translate('patient-reception_confirm.signed.label.header');
            message.value = translate('patient-reception_confirm.signed.label.message');
            break;
        case Common.CONFIRM_TYPE.DELIVERED:
            header.value = translate('patient-reception_confirm.delivered.label.header');
            message.value = translate('patient-reception_confirm.delivered.label.message');
            break;
        case Common.CONFIRM_TYPE.RECEIVED:
            header.value = translate('patient-reception_confirm.received.label.header');
            message.value = translate('patient-reception_confirm.received.label.message');
            break;
        default:
            break;
    }
});

const emit = defineEmits(['callFetchData']);

async function confirmClick() {
    confirm.require({
        group: 'confirmReception',
        accept: async () => {
            const loader = $loading.show(SETTINGS.LOADING_PROPERTIES);
            try {
            } finally {
                if (confirmType.value === Common.CONFIRM_TYPE.DELETE) {
                    await patientRequestService.delete(patient.value.id);
                }

                if (confirmType.value === Common.CONFIRM_TYPE.SIGNED) {
                    await patientRequestService.setSigned(patient.value.id);
                }

                if (confirmType.value === Common.CONFIRM_TYPE.RECEIVED) {
                    await patientRequestService.setReceived(patient.value.id);
                }

                if (confirmType.value === Common.CONFIRM_TYPE.DELIVERED) {
                    await patientRequestService.setDelivered(patient.value.id);
                }

                emit('callFetchData');
                setTimeout(() => loader.hide(), 500);
            }
        },
        reject: () => {
            toast.add({
                severity: 'error',
                summary: translate('patient-reception_confirm.label.summary'),
                detail: translate('patient-reception_confirm.label.detail'),
                life: 1000
            });
        }
    });
}

defineExpose({ confirmClick, patient, confirmType });
</script>

<template>
    <ConfirmDialog group="confirmReception">
        <template #container="{ acceptCallback, rejectCallback }">
            <div class="flex flex-col items-center p-8 bg-surface-0 dark:bg-surface-900 rounded">
                <div class="rounded-full bg-primary text-primary-contrast inline-flex justify-center items-center h-24 w-24 -mt-20">
                    <i class="pi pi-question text-5xl"></i>
                </div>
                <span class="font-bold text-2xl block mb-2 mt-6">{{ header }}</span>
                <p class="mb-0">{{ message }}</p>
                <h2 class="mb-0 red-bold">{{ patient.patientName }}</h2>
                <div class="flex items-center gap-2 mt-6">
                    <Button :label="$tt('patient-reception_confirm.button.confirm')" @click="acceptCallback"></Button>
                    <Button :label="$tt('patient-reception_confirm.button.cancel')" outlined @click="rejectCallback"></Button>
                </div>
            </div>
        </template>
    </ConfirmDialog>
</template>

<style scoped lang="scss">
.red-bold {
    color: red;
    font-weight: bold;
}
</style>
