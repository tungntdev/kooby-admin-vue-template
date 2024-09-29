<script setup>
import { useToast } from 'primevue/usetoast';
import { useConfirm } from 'primevue/useconfirm';
import { defineEmits, inject, ref, watch } from 'vue';
import PatientRequestService from '@/service/PatientRequestService';
import Common from '@/constants/common';
import { translate } from '@/locales';
import SETTINGS from '@/constants/settings';

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
        case Common.CONFIRM_TYPE.COPIED:
            header.value = translate('copy-confirm.copied.label.header');
            message.value = translate('copy-confirm.copied.label.message');
            break;
        default:
            break;
    }
});

const emit = defineEmits(['callFetchData']);

async function confirmClick() {
    confirm.require({
        group: 'confirmCopied',
        accept: async () => {
            const loader = $loading.show(SETTINGS.LOADING_PROPERTIES);
            try {
            } finally {
                if (confirmType.value === Common.CONFIRM_TYPE.COPIED) {
                    await patientRequestService.setCopied(patient.value.id);
                }
                emit('callFetchData');
                setTimeout(() => loader.hide(), 500);
            }
        },
        reject: () => {
            toast.add({
                severity: 'error',
                summary: translate('copy-confirm.copied.label.summary'),
                detail: translate('copy-confirm.copied.label.detail'),
                life: 1000
            });
        }
    });
}

defineExpose({ confirmClick, patient, confirmType });
</script>

<template>
    <ConfirmDialog group="confirmCopied">
        <template #container="{ acceptCallback, rejectCallback }">
            <div class="flex flex-col items-center p-8 bg-surface-0 dark:bg-surface-900 rounded">
                <div class="rounded-full bg-primary text-primary-contrast inline-flex justify-center items-center h-24 w-24 -mt-20">
                    <i class="pi pi-question text-5xl"></i>
                </div>
                <span class="font-bold text-2xl block mb-2 mt-6">{{ header }}</span>
                <p class="mb-0">{{ message }}</p>
                <h2 class="mb-0 red-bold">{{ patient.patientName }}</h2>
                <div class="flex items-center gap-2 mt-6">
                    <Button :label="$tt('copy-confirm.copied.button.confirm')" @click="acceptCallback"></Button>
                    <Button :label="$tt('copy-confirm.copied.button.cancel')" outlined @click="rejectCallback"></Button>
                </div>
            </div>
        </template>
    </ConfirmDialog>
</template>

<style scoped lang="scss"></style>
