<script setup>
import { defineEmits, inject, ref } from 'vue';
import FormComponent from '@/views/storage/FormComponent.vue';
import SETTINGS from '@/constants/settings';
import StorageService from '@/service/StorageService';

const visible = ref(false);
defineExpose({ visible });

const props = defineProps({
    nextNumber: null
});

const formComponentRef = ref();

const emit = defineEmits(['callFetchStorages']);
const $loading = inject('$loading');
const storageService = StorageService.INSTANCE;

async function onClickSave() {
    if (formComponentRef.value.checkValidation()) {
        const loader = $loading.show(SETTINGS.LOADING_PROPERTIES);
        try {
            await storageService.create(formComponentRef.value.dataInput);
        } finally {
            setTimeout(() => loader.hide(), 500);
            visible.value = false;
            emit('callFetchStorages');
        }
    }
}
</script>

<template>
    <Dialog v-model:visible="visible" :style="{ width: '60vw' }" :header="$tt('storage_component.title.create')" modal>
        <Fluid>
            <div class="card flex flex-col gap-6 w-full">
                <form-component ref="formComponentRef" :nextNumber="props.nextNumber"></form-component>
                <div class="flex flex-col justify-end md:flex-row gap-8">
                    <div class="flex flex-row gap-2">
                        <Button type="button" icon="pi pi-times-circle" :label="$tt('storage_component.button.cancel')" severity="secondary" @click="visible = false"></Button>
                        <Button type="button" icon="pi pi-save" :label="$tt('storage_component.button.save')" @click="onClickSave"></Button>
                    </div>
                </div>
            </div>
        </Fluid>
    </Dialog>
</template>

<style scoped lang="scss"></style>
