<script setup>
import { onBeforeMount, reactive, ref } from 'vue';
import { useCommonStore } from '@/store/commonStore';
import { storeToRefs } from 'pinia';
import PatientRequestService from '@/service/PatientRequestService';
import CommonService from '@/service/CommonService';

const props = defineProps({
    dataFormEdit: []
});

const dataInput = reactive({
    medicineCode: null,
    numberOrder: null,
    patientNumber: null,
    patientName: null,
    department: null,
    inDate: null,
    outDate: null,
    copyQuantity: 1,
    copyCost: 100000,
    receptionDate: new Date(),
    appointmentPatientDate: null,
    note: null,
    delivery: 0,
    deliveryOrderNumber: null,
    idProvince: null,
    idDistrict: null,
    deliveryCost: null,
    deliveryPhone: null,
    deliveryAddress: null
});

const validation = reactive({
    numberOrder: null,
    patientName: null,
    department: null,
    inDate: null,
    outDate: null,
    copyQuantity: null,
    copyCost: null,
    receptionDate: null,
    appointmentPatientDate: null,
    delivery: null,
    deliveryOrderNumber: null,
    idProvince: null,
    idDistrict: null,
    deliveryCost: null,
    deliveryPhone: null
});

function checkValidation() {
    const requiredFields = ['numberOrder', 'patientName', 'department', 'inDate', 'outDate', 'copyQuantity', 'copyCost', 'receptionDate', 'appointmentPatientDate'];

    if (dataInput.delivery) {
        requiredFields.push('deliveryOrderNumber', 'idProvince', 'idDistrict', 'deliveryCost', 'deliveryPhone');
    }

    const isNotEmpty = (value) => value !== null && value !== '' && value !== undefined;

    return requiredFields.every((field) => isNotEmpty(dataInput[field]));
}

function resetValidation(field) {
    validation[field] = dataInput[field] !== null && dataInput[field] !== '';
}

const commonStore = useCommonStore();
const { departments } = storeToRefs(commonStore);

const patientRequestService = PatientRequestService.INSTANCE;

const { listProvinces } = storeToRefs(commonStore);

onBeforeMount(async () => {
    await commonStore.loadDepartments();
    await commonStore.loadProvinces();
    if (props.dataFormEdit) {
        Object.assign(dataInput, props.dataFormEdit);
        if (dataInput.delivery === 1) {
            checkDelivery.value = true;
        }
        if (dataInput.idProvince != null) {
            await findDistrict();
        }
    } else {
        dataInput.numberOrder = await patientRequestService.getNextOrder();
    }
});

function copyCharge() {
    resetValidation('copyQuantity');
    dataInput.copyCost = dataInput.copyQuantity * 100000;
}

function calReturnDate() {
    resetValidation('department');
    let appointmentDate = new Date();
    appointmentDate.setDate(appointmentDate.getDate() + 14);
    if (['A6', 'A6-A', 'A6-B', 'A6-D', 'A18'].includes(dataInput.department)) {
        appointmentDate.setDate(appointmentDate.getDate() + 6);
    }
    const dayOfWeek = appointmentDate.getDay();
    if (dayOfWeek === 6) {
        appointmentDate.setDate(appointmentDate.getDate() + 2);
    } else if (dayOfWeek === 0) {
        appointmentDate.setDate(appointmentDate.getDate() + 1);
    }
    dataInput.appointmentPatientDate = appointmentDate;
}

const checkDelivery = ref(false);

async function onClickDelivery() {
    if (checkDelivery) {
        dataInput.delivery = 1;
        dataInput.deliveryOrderNumber = await patientRequestService.getDeliveryOrder();
    } else {
        dataInput.delivery = 0;
        dataInput.deliveryOrderNumber = '';
    }
}

const commonService = CommonService.INSTANCE;
const districts = ref();

async function findDistrict() {
    resetValidation('idProvince');
    const res = await commonService.loadDistrictsByProvince(dataInput.idProvince);
    if (res.state) {
        districts.value = res.payload;
    }
}

async function getDeliveryCost() {
    resetValidation('idDistrict');
    const res = await commonService.getDeliveryCost(dataInput.idDistrict);
    if (res.state) {
        dataInput.deliveryCost = res.payload.deliveryFee;
    }
}

defineExpose({ dataInput, checkValidation });
</script>

<template>
    <div class="flex flex-col md:flex-row">
        <div class="grid grid-cols-12 gap-8">
            <div class="flex flex-wrap col-span-2 gap-2 w-full">
                <label for="numberOrder">{{ $tt('patient_reception_component.label.number-order') }}<span class="text-red-500">*</span></label>
                <InputNumber id="numberOrder" :useGrouping="false" @change="resetValidation('numberOrder')" :class="{ 'p-invalid': validation.numberOrder !== null && !validation.numberOrder }" v-model="dataInput.numberOrder" />
                <small v-if="validation.numberOrder !== null && !validation.numberOrder" class="p-error">{{ $tt('patient_reception_component.label.error-input') }}</small>
            </div>
            <div class="flex flex-wrap col-span-2 gap-2 w-full">
                <label for="medicineCode">{{ $tt('patient_reception_component.label.medicine-code') }}</label>
                <InputMask id="medicineCode" mask="99/999999" placeholder="24/xxxxxx" v-model="dataInput.medicineCode" />
            </div>
            <div class="flex flex-wrap col-span-1 gap-2 w-full">
                <Button icon="pi pi-search" class="mt-8" aria-label="Search" />
            </div>
            <div class="flex flex-wrap col-span-2 gap-2 w-full">
                <label for="patientNumber">{{ $tt('patient_reception_component.label.patient-number') }}</label>
                <InputNumber id="patientNumber" :useGrouping="false" v-model="dataInput.patientNumber" />
            </div>
            <div class="flex flex-wrap col-span-3 gap-2 w-full">
                <label for="patientName">{{ $tt('patient_reception_component.label.patient-name') }}<span class="text-red-500">*</span></label>
                <InputText id="patientName" @input="resetValidation('patientName')" :class="{ 'p-invalid': validation.patientName !== null && !validation.patientName }" v-model="dataInput.patientName" />
                <small v-if="validation.patientName !== null && !validation.patientName" class="p-error">{{ $tt('patient_reception_component.label.error-input') }}</small>
            </div>
            <div class="flex flex-wrap col-span-2 gap-2 w-full">
                <label for="department">{{ $tt('patient_reception_component.label.department') }}<span class="text-red-500">*</span></label>
                <Dropdown
                    id="department"
                    @change="calReturnDate"
                    v-model="dataInput.department"
                    :options="departments"
                    editable
                    option-value="name"
                    optionLabel="name"
                    :class="{ 'p-invalid': validation.department !== null && !validation.department }"
                ></Dropdown>
                <small v-if="validation.department !== null && !validation.department" class="p-error">{{ $tt('patient_reception_component.label.error-input') }}</small>
            </div>
        </div>
    </div>

    <div class="flex flex-col md:flex-row gap-8">
        <div class="grid grid-cols-12 gap-8">
            <div class="flex flex-wrap col-span-3 gap-2 w-full">
                <label for="inDate">{{ $tt('patient_reception_component.label.in-date') }}<span class="text-red-500">*</span></label>
                <DatePicker id="inDate" showIcon dateFormat="dd/mm/yy" @input="resetValidation('inDate')" :class="{ 'p-invalid': validation.inDate !== null && !validation.inDate }" v-model="dataInput.inDate" />
                <small v-if="validation.inDate !== null && !validation.inDate" class="p-error">{{ $tt('patient_reception_component.label.error-input') }}</small>
            </div>
            <div class="flex flex-wrap col-span-3 gap-2 w-full">
                <label for="outDate">{{ $tt('patient_reception_component.label.out-date') }}<span class="text-red-500">*</span></label>
                <DatePicker id="outDate" showIcon dateFormat="dd/mm/yy" @change="resetValidation('outDate')" :class="{ 'p-invalid': validation.outDate !== null && !validation.outDate }" v-model="dataInput.outDate" />
                <small v-if="validation.outDate !== null && !validation.outDate" class="p-error">{{ $tt('patient_reception_component.label.error-input') }}</small>
            </div>
            <div class="flex flex-wrap col-span-2 gap-2 w-full">
                <label for="copyQuantity">{{ $tt('patient_reception_component.label.copy-quantity') }}<span class="text-red-500">*</span></label>
                <InputNumber id="copyQuantity" mode="decimal" :min="1" :max="100" showButtons @input="copyCharge" :class="{ 'p-invalid': validation.copyQuantity !== null && !validation.copyQuantity }" v-model="dataInput.copyQuantity" />
                <small v-if="validation.copyQuantity !== null && !validation.copyQuantity" class="p-error">{{ $tt('patient_reception_component.label.error-input') }}</small>
            </div>
            <div class="flex flex-wrap col-span-4 gap-2 w-full">
                <label for="copyCost">{{ $tt('patient_reception_component.label.copy-cost') }}<span class="text-red-500">*</span></label>
                <InputNumber id="copyCost" @input="resetValidation('copyCost')" :class="{ 'p-invalid': validation.copyCost !== null && !validation.copyCost }" v-model="dataInput.copyCost" />
                <small v-if="validation.copyCost !== null && !validation.copyCost" class="p-error">{{ $tt('patient_reception_component.label.error-input') }}</small>
            </div>
        </div>
    </div>

    <div class="flex flex-col md:flex-row gap-8">
        <div class="grid grid-cols-12 gap-8">
            <div class="flex flex-wrap col-span-3 gap-2 w-full">
                <label for="receptionDate">{{ $tt('patient_reception_component.label.reception-date') }}<span class="text-red-500">*</span></label>
                <DatePicker id="receptionDate" showIcon dateFormat="dd/mm/yy" @change="resetValidation('receptionDate')" :class="{ 'p-invalid': validation.receptionDate !== null && !validation.receptionDate }" v-model="dataInput.receptionDate" />
                <small v-if="validation.receptionDate !== null && !validation.receptionDate" class="p-error">{{ $tt('patient_reception_component.label.error-input') }}</small>
            </div>
            <div class="flex flex-wrap col-span-3 gap-2 w-full">
                <label for="appointmentPatientDate">{{ $tt('patient_reception_component.label.appointment-patient-date') }}<span class="text-red-500">*</span></label>
                <DatePicker
                    id="appointmentPatientDate"
                    dateFormat="dd/mm/yy"
                    showIcon
                    @change="resetValidation('appointmentPatientDate')"
                    :class="{ 'p-invalid': validation.appointmentPatientDate !== null && !validation.appointmentPatientDate }"
                    v-model="dataInput.appointmentPatientDate"
                />
                <small v-if="validation.appointmentPatientDate !== null && !validation.appointmentPatientDate" class="p-error">{{ $tt('patient_reception_component.label.error-input') }}</small>
            </div>
            <div class="flex flex-wrap col-span-6 gap-2 w-full">
                <label for="note">{{ $tt('patient_reception_component.label.note') }}</label>
                <InputText id="note" @input="resetValidation('note')" v-model="dataInput.note" />
            </div>
        </div>
    </div>

    <div class="flex flex-col md:flex-row gap-8">
        <div class="grid grid-cols-12 gap-8">
            <div class="flex flex-wrap col-span-3 gap-2 w-full mt-9">
                <ToggleSwitch @change="onClickDelivery" v-model="checkDelivery" />
                <label for="delivery" class="ml-2">{{ $tt('patient_reception_component.label.is-delivery') }}</label>
            </div>

            <div v-if="dataInput.delivery" class="flex flex-wrap col-span-3 gap-2 w-full">
                <label for="deliveryOrderNumber">{{ $tt('patient_reception_component.label.delivery-order-number') }}<span class="text-red-500">*</span></label>
                <InputNumber
                    id="deliveryOrderNumber"
                    :useGrouping="false"
                    @change="resetValidation('deliveryOrderNumber')"
                    :class="{ 'p-invalid': validation.deliveryOrderNumber !== null && !validation.deliveryOrderNumber }"
                    v-model="dataInput.deliveryOrderNumber"
                />
                <small v-if="validation.deliveryOrderNumber !== null && !validation.deliveryOrderNumber" class="p-error">{{ $tt('patient_reception_component.label.error-input') }}</small>
            </div>

            <div v-if="dataInput.delivery" class="flex flex-wrap col-span-3 gap-2 w-full">
                <label for="idProvince">{{ $tt('patient_reception_component.label.id-province') }}<span class="text-red-500">*</span></label>
                <Dropdown
                    id="idProvince"
                    v-model="dataInput.idProvince"
                    :options="listProvinces"
                    editable
                    @change="findDistrict"
                    option-value="id"
                    optionLabel="name"
                    :class="{ 'p-invalid': validation.idProvince !== null && !validation.idProvince }"
                ></Dropdown>
                <small v-if="validation.idProvince !== null && !validation.idProvince" class="p-error">{{ $tt('patient_reception_component.label.error-input') }}</small>
            </div>

            <div v-if="dataInput.delivery" class="flex flex-wrap col-span-3 gap-2 w-full">
                <label for="idDistrict">{{ $tt('patient_reception_component.label.id-district') }}<span class="text-red-500">*</span></label>
                <Dropdown
                    id="idDistrict"
                    @change="getDeliveryCost()"
                    v-model="dataInput.idDistrict"
                    :options="districts"
                    editable
                    option-value="id"
                    optionLabel="name"
                    :class="{ 'p-invalid': validation.idDistrict !== null && !validation.idDistrict }"
                ></Dropdown>
                <small v-if="validation.idDistrict !== null && !validation.idDistrict" class="p-error">{{ $tt('patient_reception_component.label.error-input') }}</small>
            </div>
        </div>
    </div>

    <div v-if="dataInput.delivery" class="flex flex-col md:flex-row gap-8">
        <div class="grid grid-cols-12 gap-8">
            <div class="flex flex-wrap col-span-3 gap-2 w-full">
                <label for="deliveryCost">{{ $tt('patient_reception_component.label.delivery-cost') }}<span class="text-red-500">*</span></label>
                <InputNumber id="deliveryCost" @input="resetValidation('deliveryCost')" :class="{ 'p-invalid': validation.deliveryCost !== null && !validation.deliveryCost }" v-model="dataInput.deliveryCost" />
                <small v-if="validation.deliveryCost !== null && !validation.deliveryCost" class="p-error">{{ $tt('patient_reception_component.label.error-input') }}</small>
            </div>

            <div class="flex flex-wrap col-span-3 gap-2 w-full">
                <label for="deliveryPhone">{{ $tt('patient_reception_component.label.delivery-phone') }}<span class="text-red-500">*</span></label>
                <InputText id="deliveryPhone" @input="resetValidation('deliveryPhone')" :class="{ 'p-invalid': validation.deliveryPhone !== null && !validation.deliveryPhone }" v-model="dataInput.deliveryPhone" />
                <small v-if="validation.deliveryPhone !== null && !validation.deliveryPhone" class="p-error">{{ $tt('patient_reception_component.label.error-input') }}</small>
            </div>

            <div class="flex flex-wrap col-span-6 gap-2 w-full">
                <label for="deliveryAddress">{{ $tt('patient_reception_component.label.delivery-address') }}</label>
                <InputText id="deliveryAddress" @input="resetValidation('note')" v-model="dataInput.deliveryAddress" />
            </div>
        </div>
    </div>
</template>

<style scoped lang="scss"></style>
