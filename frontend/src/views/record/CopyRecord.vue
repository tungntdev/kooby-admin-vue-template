<script setup>
import { onBeforeMount, ref } from 'vue';

onBeforeMount(async () => {});

const visible = ref(false);
const patientData = ref([]);
defineExpose({ patientData, visible });

async function copyClick() {
    if (patientData.value.patientNumber) {
    }
}
</script>

<template>
    <Dialog v-model:visible="visible" modal :header="$tt('copy-record.header')" :style="{ width: '50rem' }">
        <Fluid>
            <span class="text-surface-500 dark:text-surface-400 block mb-4">{{ $tt('copy-record.title') }}</span>
            <div class="flex flex-col md:flex-row">
                <div class="grid grid-cols-12 gap-8">
                    <div class="flex flex-wrap col-span-3 gap-2 w-full">
                        <label for="numberOrder">{{ $tt('copy-record.label.number-order') }}</label>
                        <InputNumber disabled id="numberOrder" :useGrouping="false" v-model="patientData.numberOrder" />
                    </div>
                    <div class="flex flex-wrap col-span-4 gap-2 w-full">
                        <label for="department">{{ $tt('copy-record.label.department') }}</label>
                        <InputText disabled id="department" v-model="patientData.department" />
                    </div>
                    <div class="flex flex-wrap col-span-5 gap-2 w-full">
                        <label for="patientName">{{ $tt('copy-record.label.patient-name') }}</label>
                        <InputText disabled id="patientName" v-model="patientData.patientName" />
                    </div>
                </div>
            </div>

            <div class="flex flex-col md:flex-row gap-8 mt-6">
                <div class="grid grid-cols-12 gap-8">
                    <div class="flex flex-wrap col-span-3 gap-2 w-full">
                        <label for="numberOrder">{{ $tt('copy-record.label.patient-number') }}</label>
                        <InputNumber id="patientNumber" :useGrouping="false" v-model="patientData.patientNumber" />
                    </div>
                    <div class="flex flex-wrap col-span-3 gap-2 w-full">
                        <label for="inDate">{{ $tt('copy-record.label.in-date') }}</label>
                        <DatePicker disabled id="inDate" showIcon dateFormat="dd/mm/yy" v-model="patientData.inDate" />
                    </div>
                    <div class="flex flex-wrap col-span-3 gap-2 w-full">
                        <label for="outDate">{{ $tt('copy-record.label.out-date') }}</label>
                        <DatePicker disabled id="outDate" dateFormat="dd/mm/yy" showIcon v-model="patientData.outDate" />
                    </div>
                    <div class="flex flex-wrap col-span-3 gap-2 w-full">
                        <label for="appointmentCopyDate">{{ $tt('copy-record.label.appointment-date') }}</label>
                        <DatePicker disabled id="appointmentCopyDate" :minDate="minDate" :maxDate="maxDate" dateFormat="dd/mm/yy" showIcon v-model="patientData.appointmentDate" />
                    </div>
                </div>
            </div>

            <div class="flex flex-col justify-end md:flex-row mt-6 gap-8">
                <div class="flex flex-row gap-2">
                    <Button type="button" icon="pi pi-times-circle" :label="$tt('copy-record.button.cancel')" severity="secondary" @click="visible = false"></Button>
                    <Button type="button" icon="pi pi-file-word" :label="$tt('copy-record.button.copy')" @click="copyClick"></Button>
                </div>
            </div>
        </Fluid>
    </Dialog>
</template>

<style lang="scss" scoped></style>
