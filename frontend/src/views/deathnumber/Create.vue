<script setup>
import { defineEmits, inject, ref } from 'vue';
import FormComponent from '@/views/deathnumber/FormComponent.vue';
import DeathNumberService from '@/service/DeathNumberService';
import SETTINGS from '@/constants/settings';

const deathNumberService = DeathNumberService.INSTANCE;

const visible = ref(false);
defineExpose({ visible });
const formComponentRef = ref();

const props = defineProps({
    nextNumber: null
});

const $loading = inject('$loading');

const emit = defineEmits(['callFetchDeathNumbers']);

async function onClickSave() {
    if (formComponentRef.value.checkValidation()) {
        const loader = $loading.show(SETTINGS.LOADING_PROPERTIES);
        try {
            await deathNumberService.create(formComponentRef.value.dataInput);
        } finally {
            setTimeout(() => loader.hide(), 500);
            visible.value = false;
            emit('callFetchDeathNumbers');
        }
    }
}
</script>

<template>
    <Dialog v-model:visible="visible" :style="{ width: '60vw' }" :header="$tt('death-number-form-component.title.create')" modal>
        <Fluid>
            <div class="card flex flex-col gap-6 w-full">
                <form-component ref="formComponentRef" :nextNumber="props.nextNumber"></form-component>
                <div class="flex flex-col justify-end md:flex-row gap-8">
                    <div class="flex flex-row gap-2">
                        <Button type="button" icon="pi pi-times-circle" :label="$tt('death-number-form-component.button.cancel')" severity="secondary" @click="visible = false"></Button>
                        <Button type="button" icon="pi pi-save" :label="$tt('death-number-form-component.button.save')" @click="onClickSave"></Button>
                    </div>
                </div>
            </div>
        </Fluid>
    </Dialog>
</template>

<style lang="scss" scoped></style>
