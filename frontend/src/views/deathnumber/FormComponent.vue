<script setup>
import { onBeforeMount, reactive, ref, watch } from 'vue';
import { useCommonStore } from '@/store/commonStore';
import { storeToRefs } from 'pinia';

const props = defineProps({
    dataFormEdit: [],
    nextNumber: null
});

const updateDataInput = async () => {
    if (props.dataFormEdit) {
        Object.assign(dataInput, props.dataFormEdit);
        if (props.dataFormEdit.receptionDate) {
            dataInput.receptionDate = new Date(props.dataFormEdit.receptionDate);
        }
        if (props.dataFormEdit.submitterRecordDate) {
            dataInput.submitterRecordDate = new Date(props.dataFormEdit.submitterRecordDate);
        }
    } else if (props.nextNumber !== null) {
        dataInput.deathNumber = props.nextNumber;
    }
};

watch(
    () => [props.dataFormEdit, props.nextNumber],
    async () => {
        await updateDataInput();
    },
    { immediate: true }
);
const dataInput = reactive({
    deathNumber: null,
    patientName: null,
    patientNumber: null,
    department: null,
    sex: null,
    birthday: null,
    address: null,
    deathDate: null,
    receptionDate: new Date(),
    submitter: null,
    receiver: null,
    diagnosisOfDeath: null,
    statusRecord: 0,
    submitterRecord: null,
    submitterRecordDate: null
});

const validation = reactive({
    deathNumber: null,
    patientName: null,
    patientNumber: null,
    department: null,
    sex: null,
    birthday: null,
    deathDate: null,
    receptionDate: null,
    submitter: null,
    diagnosisOfDeath: null
});

function checkValidation() {
    const requiredFields = ['deathNumber', 'patientName', 'patientNumber', 'department', 'sex', 'birthday', 'deathDate', 'receptionDate', 'submitter', 'diagnosisOfDeath'];

    const isNotEmpty = (value) => value !== null && value !== '';

    requiredFields.forEach((field) => {
        validation[field] = isNotEmpty(dataInput[field]);
    });

    return Object.values(validation).every((value) => value);
}

function resetValidation(field) {
    validation[field] = dataInput[field] !== null && dataInput[field] !== '';
}

const sexList = ref([
    { name: 'Nam', code: 'NA' },
    { name: 'Nữ', code: 'NU' },
    { name: 'Không xác định', code: 'KXD' }
]);

const commonStore = useCommonStore();
const { departments } = storeToRefs(commonStore);

onBeforeMount(async () => {
    await commonStore.loadDepartments();
    console.debug(departments);
});

defineExpose({ dataInput, validation, checkValidation });
</script>

<template>
    <div class="flex flex-col md:flex-row">
        <div class="grid grid-cols-12 gap-8">
            <div class="flex flex-wrap col-span-3 gap-2 w-full">
                <label for="deathNumber">{{ $tt('death-number-form-component.label.death-number') }}<span class="text-red-500">*</span></label>
                <InputNumber id="deathNumber" @change="resetValidation('deathNumber')" :class="{ 'p-invalid': validation.deathNumber !== null && !validation.deathNumber }" v-model="dataInput.deathNumber" />
                <small v-if="validation.deathNumber !== null && !validation.deathNumber" class="p-error">{{ $tt('death-number-form-component.label.error-input') }}</small>
            </div>
            <div class="flex flex-wrap col-span-3 gap-2 w-full">
                <label for="patientNumber">{{ $tt('death-number-form-component.label.patient-number') }}<span class="text-red-500">*</span></label>
                <InputNumber id="patientNumber" @change="resetValidation('patientNumber')" :class="{ 'p-invalid': validation.patientNumber !== null && !validation.patientNumber }" v-model="dataInput.patientNumber" />
                <small v-if="validation.patientNumber !== null && !validation.patientNumber" class="p-error">{{ $tt('death-number-form-component.label.error-input') }}</small>
            </div>
            <div class="flex flex-wrap col-span-4 gap-2 w-full">
                <label for="patientName">{{ $tt('death-number-form-component.label.patient-name') }}<span class="text-red-500">*</span></label>
                <InputText id="patientName" @input="resetValidation('patientName')" :class="{ 'p-invalid': validation.patientName !== null && !validation.patientName }" v-model="dataInput.patientName" />
                <small v-if="validation.patientName !== null && !validation.patientName" class="p-error">{{ $tt('death-number-form-component.label.error-input') }}</small>
            </div>
            <div class="flex flex-wrap col-span-2 gap-2 w-full">
                <label for="department">{{ $tt('death-number-form-component.label.department') }}<span class="text-red-500">*</span></label>
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
                <small v-if="validation.department !== null && !validation.department" class="p-error">{{ $tt('death-number-form-component.label.error-input') }}</small>
            </div>
        </div>
    </div>

    <div class="flex flex-col md:flex-row gap-8">
        <div class="grid grid-cols-12 gap-8">
            <div class="flex flex-wrap col-span-3 gap-2 w-full">
                <label for="sex">{{ $tt('death-number-form-component.label.sex') }}</label>
                <Dropdown id="sex" v-model="dataInput.sex" :options="sexList" editable option-value="name" optionLabel="name"></Dropdown>
            </div>
            <div class="flex flex-wrap col-span-3 gap-2 w-full">
                <label for="birthday">{{ $tt('death-number-form-component.label.birthday') }}<span class="text-red-500">*</span></label>
                <InputText id="birthday" @input="resetValidation('birthday')" :class="{ 'p-invalid': validation.birthday !== null && !validation.birthday }" v-model="dataInput.birthday" />
                <small v-if="validation.birthday !== null && !validation.birthday" class="p-error">{{ $tt('death-number-form-component.label.error-input') }}</small>
            </div>
            <div class="flex flex-wrap col-span-4 gap-2 w-full">
                <label for="deathDate">{{ $tt('death-number-form-component.label.death-date') }}<span class="text-red-500">*</span></label>
                <InputText id="deathDate" @input="resetValidation('deathDate')" :class="{ 'p-invalid': validation.deathDate !== null && !validation.deathDate }" v-model="dataInput.deathDate" />
                <small v-if="validation.deathDate !== null && !validation.deathDate" class="p-error">{{ $tt('death-number-form-component.label.error-input') }}</small>
            </div>
            <div class="flex flex-wrap col-span-2 gap-2 w-full">
                <label for="receptionDate">{{ $tt('death-number-form-component.label.reception-date') }}<span class="text-red-500">*</span></label>
                <DatePicker id="receptionDate" dateFormat="dd/mm/yy" @change="resetValidation('receptionDate')" :class="{ 'p-invalid': validation.receptionDate !== null && !validation.receptionDate }" v-model="dataInput.receptionDate" />
                <small v-if="validation.receptionDate !== null && !validation.receptionDate" class="p-error">{{ $tt('death-number-form-component.label.error-input') }}</small>
            </div>
        </div>
    </div>

    <div class="flex flex-col md:flex-row gap-8">
        <div class="flex flex-wrap gap-2 w-full">
            <label for="address">{{ $tt('death-number-form-component.label.address') }}</label>
            <InputText id="address" v-model="dataInput.address" />
        </div>
    </div>

    <div class="flex flex-col md:flex-row gap-8">
        <div class="flex flex-wrap gap-2 w-full">
            <label for="diagnosisOfDeath">{{ $tt('death-number-form-component.label.diagnosis-of-death') }}<span class="text-red-500">*</span></label>
            <Textarea id="diagnosisOfDeath" @input="resetValidation('diagnosisOfDeath')" v-model="dataInput.diagnosisOfDeath" :class="{ 'p-invalid': validation.diagnosisOfDeath !== null && !validation.diagnosisOfDeath }" />
            <small v-if="validation.diagnosisOfDeath !== null && !validation.diagnosisOfDeath" class="p-error">{{ $tt('death-number-form-component.label.error-input') }}</small>
        </div>
    </div>

    <div class="flex flex-col md:flex-row gap-8">
        <div class="grid grid-cols-12 gap-8">
            <div class="flex flex-wrap col-span-5 gap-2 w-full">
                <label for="submitter">{{ $tt('death-number-form-component.label.submitter') }}<span class="text-red-500">*</span></label>
                <InputText id="submitter" @change="resetValidation('submitter')" :class="{ 'p-invalid': validation.submitter !== null && !validation.submitter }" v-model="dataInput.submitter" />
                <small v-if="validation.submitter !== null && !validation.submitter" class="p-error">{{ $tt('death-number-form-component.label.error-input') }}</small>
            </div>
            <div class="flex flex-wrap col-span-5 gap-2 w-full">
                <label for="submitterRecord">{{ $tt('death-number-form-component.label.submitter-record') }}</label>
                <InputText id="submitterRecord" v-model="dataInput.submitterRecord" />
            </div>
            <div class="flex flex-wrap col-span-2 gap-2 w-full">
                <label for="submitterRecordDate">{{ $tt('death-number-form-component.label.submitter-record-date') }}</label>
                <DatePicker dateFormat="dd/mm/yy" id="submitterRecordDate" v-model="dataInput.submitterRecordDate" />
            </div>
        </div>
    </div>
</template>

<style scoped lang="scss"></style>
