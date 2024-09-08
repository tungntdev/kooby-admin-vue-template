<script setup>
import { useToast } from 'primevue/usetoast';
import { defineEmits, inject, ref } from 'vue';
import DeathNumberService from '@/service/DeathNumberService';
import SETTINGS from '@/constants/settings';
import { useConfirm } from 'primevue/useconfirm';
import { translate } from '@/locales';

const toast = useToast();
const confirm1 = useConfirm();
const idDeathNumber = ref();
const $loading = inject('$loading');
const deathNumberService = DeathNumberService.INSTANCE;

const emit = defineEmits(['callFetchDeathNumbers']);

async function confirmDelete() {
    confirm1.require({
        group: 'confirmDelete',
        accept: async () => {
            const loader = $loading.show(SETTINGS.LOADING_PROPERTIES);
            try {
                await deathNumberService.delete(idDeathNumber.value);
            } finally {
                emit('callFetchDeathNumbers');
                setTimeout(() => loader.hide(), 500);
            }
        },
        reject: () => {
            toast.add({
                severity: 'error',
                summary: translate('death-number-confirm-delete.label.summary'),
                detail: translate('death-number-confirm-delete.label.detail'),
                life: 1000
            });
        }
    });
}

defineExpose({ confirmDelete, idDeathNumber });
</script>

<template>
    <ConfirmDialog group="confirmDelete">
        <template #container="{ acceptCallback, rejectCallback }">
            <div class="flex flex-col items-center p-8 bg-surface-0 dark:bg-surface-900 rounded">
                <div class="rounded-full bg-primary text-primary-contrast inline-flex justify-center items-center h-24 w-24 -mt-20">
                    <i class="pi pi-question text-5xl"></i>
                </div>
                <span class="font-bold text-2xl block mb-2 mt-6">{{ $tt('death-number-confirm-delete.label.header') }}</span>
                <p class="mb-0">{{ $tt('death-number-confirm-delete.label.message') }}</p>
                <div class="flex items-center gap-2 mt-6">
                    <Button :label="$tt('death-number-confirm-delete.button.confirm')" @click="acceptCallback"></Button>
                    <Button :label="$tt('death-number-confirm-delete.button.cancel')" outlined @click="rejectCallback"></Button>
                </div>
            </div>
        </template>
    </ConfirmDialog>
</template>

<style scoped lang="scss"></style>
