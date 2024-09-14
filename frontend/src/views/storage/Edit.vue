<script setup>
import { defineEmits, inject, ref } from 'vue';
import SETTINGS from '@/constants/settings';
import FormComponent from '@/views/storage/FormComponent.vue';
import StorageService from '@/service/StorageService';

const visible = ref(false);
const props = defineProps({
    dataEdit: []
});
const formComponentRef = ref();
defineExpose({ visible, formComponentRef });

const $loading = inject('$loading');
const storageService = StorageService.INSTANCE;
const emit = defineEmits(['callFetchStorages']);

async function onClickUpdate() {
    if (formComponentRef.value.checkValidation()) {
        const loader = $loading.show(SETTINGS.LOADING_PROPERTIES);
        try {
            await storageService.update(formComponentRef.value.dataInput);
        } finally {
            setTimeout(() => loader.hide(), 500);
            visible.value = false;
            await emit('callFetchStorages');
        }
    }
}
</script>

<template>
    <Dialog v-model:visible="visible" :style="{ width: '60vw' }" :header="$tt('storage_component.title.update')" modal>
        <Fluid>
            <div class="card flex flex-col gap-6 w-full">
                <form-component ref="formComponentRef" :dataFormEdit="props.dataEdit"></form-component>
                <div class="flex flex-col justify-end md:flex-row gap-8">
                    <div class="flex flex-row gap-2">
                        <Button type="button" :label="$tt('storage_component.button.cancel')" severity="secondary" @click="visible = false"></Button>
                        <Button type="button" :label="$tt('storage_component.button.update')" @click="onClickUpdate"></Button>
                    </div>
                </div>
            </div>
        </Fluid>
    </Dialog>
</template>

<style scoped lang="scss"></style>
