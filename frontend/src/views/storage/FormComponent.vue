<script setup>
import { onBeforeMount, reactive, ref } from 'vue';

const props = defineProps({
    dataFormEdit: []
});

onBeforeMount(async () => {
    if (props.dataFormEdit) {
        Object.assign(dataInput, props.dataFormEdit);
        dataInput.inDate = new Date(props.dataFormEdit.inDate);
        dataInput.outDate = new Date(props.dataFormEdit.outDate);
    } else {
    }
});

const dataInput = reactive({
    saveNumber: null,
    patientName: null,
    patientNumber: null,
    department: null,
    sex: null,
    birthday: null,
    inDate: null,
    outDate: null,
    saveYear: null,
    diagnose: null
});

const validation = reactive({
    saveNumber: null,
    patientName: null,
    patientNumber: null,
    department: null,
    sex: null,
    birthday: null,
    inDate: null,
    outDate: null,
    saveYear: null,
    diagnose: null
});

const departments = ref([
    { name: 'A6-A', code: 'A6-A' },
    { name: 'A6-B', code: 'A6-B' },
    { name: 'A6-D', code: 'A6-D' },
    { name: 'A6-C', code: 'A6-C' },
    { name: 'A18', code: 'A18' },
    { name: 'A15', code: 'A15' }
]);

const sexList = ref([
    { name: 'Nam', code: 'NA' },
    { name: 'Nữ', code: 'NU' },
    { name: 'Không xác định', code: 'KXD' }
]);

function checkValidation() {
    const requiredFields = ['saveNumber', 'patientName', 'patientNumber', 'department', 'sex', 'birthday', 'inDate', 'outDate', 'saveYear', 'diagnose'];

    const isNotEmpty = (value) => value !== null && value !== '';

    requiredFields.forEach((field) => {
        validation[field] = isNotEmpty(dataInput[field]);
    });

    return Object.values(validation).every((value) => value);
}

function resetValidation(field) {
    validation[field] = dataInput[field] !== null && dataInput[field] !== '';
}

defineExpose({ dataInput, validation, checkValidation });
</script>

<template>
    <div class="flex flex-col md:flex-row">
        <div class="grid grid-cols-12 gap-8">
            <div class="flex flex-wrap col-span-2 gap-2 w-full">
                <label for="department">{{ $tt('storage_component.label.department') }}<span class="text-red-500">*</span></label>
                <Dropdown
                    id="department"
                    @change="resetValidation('department')"
                    v-model="dataInput.department"
                    :options="departments"
                    editable
                    option-value="name"
                    optionLabel="name"
                    :class="{ 'p-invalid': validation.department !== null && !validation.department }"
                ></Dropdown>
                <small v-if="validation.department !== null && !validation.department" class="p-error">{{ $tt('storage_component.label.error-input') }}</small>
            </div>
            <div class="flex flex-wrap col-span-2 gap-2 w-full">
                <label for="saveNumber">{{ $tt('storage_component.label.save-number') }}<span class="text-red-500">*</span></label>
                <InputNumber id="saveNumber" :useGrouping="false" @change="resetValidation('saveNumber')" :class="{ 'p-invalid': validation.saveNumber !== null && !validation.saveNumber }" style="font-weight: bold" v-model="dataInput.saveNumber" />
                <small v-if="validation.saveNumber !== null && !validation.saveNumber" class="p-error">{{ $tt('storage_component.label.error-input') }}</small>
            </div>
            <div class="flex flex-wrap col-span-3 gap-2 w-full">
                <label for="patientNumber">{{ $tt('storage_component.label.patient-number') }}<span class="text-red-500">*</span></label>
                <InputNumber id="patientNumber" :useGrouping="false" @change="resetValidation('patientNumber')" :class="{ 'p-invalid': validation.patientNumber !== null && !validation.patientNumber }" v-model="dataInput.patientNumber" />
                <small v-if="validation.patientNumber !== null && !validation.patientNumber" class="p-error">{{ $tt('storage_component.label.error-input') }}</small>
            </div>
            <div class="flex flex-wrap col-span-1 gap-2 w-full">
                <Button icon="pi pi-search" class="mt-8" rounded outlined aria-label="Search" />
            </div>
            <div class="flex flex-wrap col-span-4 gap-2 w-full">
                <label for="patientName">{{ $tt('storage_component.label.patient-name') }}<span class="text-red-500">*</span></label>
                <InputText id="patientName" @input="resetValidation('patientName')" :class="{ 'p-invalid': validation.patientName !== null && !validation.patientName }" v-model="dataInput.patientName" />
                <small v-if="validation.patientName !== null && !validation.patientName" class="p-error">{{ $tt('storage_component.label.error-input') }}</small>
            </div>
        </div>
    </div>

    <div class="flex flex-col md:flex-row gap-8">
        <div class="grid grid-cols-12 gap-8">
            <div class="flex flex-wrap col-span-2 gap-2 w-full">
                <label for="sex">{{ $tt('storage_component.label.sex') }}<span class="text-red-500">*</span></label>
                <Dropdown id="sex" v-model="dataInput.sex" @change="resetValidation('sex')" :class="{ 'p-invalid': validation.sex !== null && !validation.sex }" :options="sexList" editable option-value="name" optionLabel="name"></Dropdown>
                <small v-if="validation.sex !== null && !validation.sex" class="p-error">{{ $tt('storage_component.label.error-input') }}</small>
            </div>
            <div class="flex flex-wrap col-span-2 gap-2 w-full">
                <label for="birthday">{{ $tt('storage_component.label.birthday') }}<span class="text-red-500">*</span></label>
                <InputText id="birthday" @input="resetValidation('birthday')" :class="{ 'p-invalid': validation.birthday !== null && !validation.birthday }" v-model="dataInput.birthday" />
                <small v-if="validation.birthday !== null && !validation.birthday" class="p-error">{{ $tt('storage_component.label.error-input') }}</small>
            </div>
            <div class="flex flex-wrap col-span-3 gap-2 w-full">
                <label for="inDate">{{ $tt('storage_component.label.in-date') }}<span class="text-red-500">*</span></label>
                <DatePicker id="inDate" dateFormat="dd/mm/yy" @change="resetValidation('inDate')" :class="{ 'p-invalid': validation.inDate !== null && !validation.inDate }" v-model="dataInput.inDate" />
                <small v-if="validation.inDate !== null && !validation.inDate" class="p-error">{{ $tt('storage_component.label.error-input') }}</small>
            </div>
            <div class="flex flex-wrap col-span-3 gap-2 w-full">
                <label for="outDate">{{ $tt('storage_component.label.out-date') }}<span class="text-red-500">*</span></label>
                <DatePicker id="outDate" dateFormat="dd/mm/yy" @change="resetValidation('outDate')" :class="{ 'p-invalid': validation.outDate !== null && !validation.outDate }" v-model="dataInput.outDate" />
                <small v-if="validation.outDate !== null && !validation.outDate" class="p-error">{{ $tt('storage_component.label.error-input') }}</small>
            </div>
            <div class="flex flex-wrap col-span-2 gap-2 w-full">
                <label for="saveYear">{{ $tt('storage_component.label.save-year') }}<span class="text-red-500">*</span></label>
                <InputNumber id="saveYear" :useGrouping="false" @change="resetValidation('saveYear')" :class="{ 'p-invalid': validation.saveYear !== null && !validation.saveYear }" v-model="dataInput.saveYear" />
                <small v-if="validation.saveYear !== null && !validation.saveYear" class="p-error">{{ $tt('storage_component.label.error-input') }}</small>
            </div>
        </div>
    </div>

    <div class="flex flex-col md:flex-row gap-8">
        <div class="flex flex-wrap gap-2 w-full">
            <label for="diagnose">{{ $tt('storage_component.label.diagnose') }}<span class="text-red-500">*</span></label>
            <Textarea id="diagnose" @input="resetValidation('diagnose')" v-model="dataInput.diagnose" :class="{ 'p-invalid': validation.diagnose !== null && !validation.diagnose }" />
            <small v-if="validation.diagnose !== null && !validation.diagnose" class="p-error">{{ $tt('storage_component.label.error-input') }}</small>
        </div>
    </div>
</template>

<style scoped lang="scss"></style>
