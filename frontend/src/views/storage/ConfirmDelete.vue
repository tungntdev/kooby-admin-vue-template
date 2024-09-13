<script setup>
import { useToast } from 'primevue/usetoast';
import { useConfirm } from 'primevue/useconfirm';
import { defineEmits, inject, ref } from 'vue';
import SETTINGS from '@/constants/settings';
import { translate } from '@/locales';
import StorageService from '@/service/StorageService';

const toast = useToast();
const confirm2 = useConfirm();
const idStorage = ref();
const $loading = inject('$loading');
const storageService = StorageService.INSTANCE;

const emit = defineEmits(['callFetchStorages']);

async function confirmDelete() {
    confirm2.require({
        group: 'confirmDeleteStorage',
        accept: async () => {
            const loader = $loading.show(SETTINGS.LOADING_PROPERTIES);
            try {
                await storageService.delete(idStorage.value);
            } finally {
                emit('callFetchStorages');
                setTimeout(() => loader.hide(), 500);
            }
        },
        reject: () => {
            toast.add({
                severity: 'error',
                summary: translate('confirm-delete.label.summary'),
                detail: translate('confirm-delete.label.detail'),
                life: 1000
            });
        }
    });
}

defineExpose({ confirmDelete, idStorage });
</script>

<template>
    <ConfirmDialog group="confirmDeleteStorage">
        <template #container="{ acceptCallback, rejectCallback }">
            <div class="flex flex-col items-center p-8 bg-surface-0 dark:bg-surface-900 rounded">
                <div class="rounded-full bg-primary text-primary-contrast inline-flex justify-center items-center h-24 w-24 -mt-20">
                    <i class="pi pi-question text-5xl"></i>
                </div>
                <span class="font-bold text-2xl block mb-2 mt-6">{{ $tt('confirm-delete.label.header') }}</span>
                <p class="mb-0">{{ $tt('confirm-delete.label.message') }}</p>
                <div class="flex items-center gap-2 mt-6">
                    <Button :label="$tt('confirm-delete.button.confirm')" @click="acceptCallback"></Button>
                    <Button :label="$tt('confirm-delete.button.cancel')" severity="secondary" outlined @click="rejectCallback"></Button>
                </div>
            </div>
        </template>
    </ConfirmDialog>
</template>

<style scoped lang="scss"></style>
