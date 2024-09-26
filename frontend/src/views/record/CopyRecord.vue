<script setup>
import { onBeforeMount, ref } from 'vue'
import PizZip from 'pizzip'
import Docxtemplater from 'docxtemplater'
import router from '@/router'
import { useRoute } from 'vue-router';

const soHoSo = ref('')
const MaBN = ref('')
const SoLuu = ref('')
const Khoa = ref('')
const MaKhoa2 = ref('')
const MaKhoa = ref()
const MaYTe = ref('')
const HoTen = ref('')
const NgaySinh = ref('')
const Tuoi = ref('')
const Xa = ref('')
const Huyen = ref('')
const Tinh = ref('')
const GioiTinh = ref('')
const DiaChi = ref('')
const MaTheBH = ref('')
const CanCuocCongDan = ref('')
const NgayVaoVien = ref('')
const NgayRaVien = ref('')
const ChanDoanVaoVien = ref('')
const ChanDoanRaVien = ref('')
const PPDieuTri = ref('')
const TinhTrangRaVien = ref('')
const QuaTrinhBenhLy = ref('')
const LyDoVaoVien = ref('')
const TienSuBenh = ref('')
const DauHieuLamSang = ref('')
const patientDetail = ref('')
const benhAnNoiTru = ref('')
const ketQuaXN = ref()
const SieuAm = ref('')
const NoiSoi = ref('')
const Xquang = ref('')
const DienTim = ref('')
const MRI = ref('')
const PET = ref('')
const Scanner = ref('')
const GiaiPhauBenh = ref('')
const HuongDieuTri = ref('')
const soHoSoXN = ref('')

const route = useRoute();

onBeforeMount(async ()=>{
    soHoSo.value = route.query.soHS;
    console.debug(soHoSo.value)
    if(soHoSo.value){
        await searchClick()
    }
})


const TaoFileSaoBenhAn = async () => {
    await updateDaSaoBA()
    if (!MaKhoa.value) {
        MaKhoa.value = Khoa.value
    }
    // Tải mẫu Word từ một nguồn nào đó
    const docxUrl = 'fileMau/MauSaoBA.docx'
    const templateData = await fetch(docxUrl).then((res) => res.arrayBuffer())

    // Tạo một đối tượng PizZip và đối tượng Docxtemplater
    const zip = new PizZip(templateData)
    const doc = new Docxtemplater(zip)
    let thuaLenh1 = 'TP. KẾ HOẠCH TỔNG HỢP'
    let thuaLenh2 = ''
    let nguoiKy = 'Đại tá BSCKII. Bùi Việt Hùng'
    let PPDieuTriNoi = '\u2611  Không     \u2610  Có, ghi rõ:'
    let PPDieuTriNgoai = '\u2610  Không     \u2611  Có, ghi rõ phương pháp: ' + PPDieuTri.value
    if (!MaKhoa2.value) {
        MaKhoa2.value = 'KXD'
    }
    if (MaKhoa.value.charAt(0) === 'A' || MaKhoa2.value.includes('A20')) {
        thuaLenh1 = 'KT. TP KẾ HOẠCH TỔNG HỢP'
        thuaLenh2 = 'PHÓ TRƯỞNG PHÒNG'
        nguoiKy = 'Trung tá ThS. Nguyễn Xuân Tuyên'
        PPDieuTriNoi = '\u2610  Không     \u2611  Có, ghi rõ: ' + PPDieuTri.value
        PPDieuTriNgoai = '\u2611  Không     \u2610  Có, ghi rõ phương pháp:'
    }
    let KQDT =
        '\u2610 Khỏi     \u2610 Đỡ      \u2610 Không thay đổi    \u2610 Nặng hơn  \u2610 Tử vong ' +
        '\u2610 Tiên lượng nặng xin về        \u2610 Chưa xác định \n'
    if (TinhTrangRaVien.value === '1') {
        KQDT =
            '\u2611 Khỏi     \u2610 Đỡ      \u2610 Không thay đổi    \u2610 Nặng hơn  \u2610 Tử vong ' +
            '\u2610 Tiên lượng nặng xin về        \u2610 Chưa xác định \n'
    }
    if (TinhTrangRaVien.value === '2') {
        KQDT =
            '\u2610 Khỏi     \u2611 Đỡ      \u2610 Không thay đổi    \u2610 Nặng hơn  \u2610 Tử vong ' +
            '\u2610 Tiên lượng nặng xin về        \u2610 Chưa xác định \n'
    }
    if (TinhTrangRaVien.value === '3') {
        KQDT =
            '\u2610 Khỏi     \u2610 Đỡ      \u2611 Không thay đổi    \u2610 Nặng hơn  \u2610 Tử vong ' +
            '\u2610 Tiên lượng nặng xin về        \u2610 Chưa xác định \n'
    }
    if (TinhTrangRaVien.value === '4') {
        KQDT =
            '\u2610 Khỏi     \u2610 Đỡ      \u2610 Không thay đổi    \u2611 Nặng hơn  \u2610 Tử vong ' +
            '\u2610 Tiên lượng nặng xin về        \u2610 Chưa xác định \n'
    }
    if (TinhTrangRaVien.value === '5') {
        KQDT =
            '\u2610 Khỏi     \u2610 Đỡ      \u2610 Không thay đổi    \u2610 Nặng hơn  \u2611 Tử vong ' +
            '\u2610 Tiên lượng nặng xin về        \u2610 Chưa xác định \n'
    }
    if (TinhTrangRaVien.value === '7') {
        KQDT =
            '\u2610 Khỏi     \u2610 Đỡ      \u2610 Không thay đổi    \u2610 Nặng hơn  \u2610 Tử vong ' +
            '\u2611 Tiên lượng nặng xin về        \u2610 Chưa xác định \n'
    }
    // Dữ liệu để điền vào mẫu Word
    if (!MaKhoa.value) {
        MaKhoa.value = Khoa.value
    }
    let data = {
        Khoa: Khoa.value,
        MaYTe: MaYTe.value,
        SoLuu: SoLuu.value,
        SoHoSo: soHoSo.value,
        HoTen: HoTen.value,
        NgaySinh: NgaySinh.value,
        Tuoi: Tuoi.value,
        Xa: Xa.value,
        Huyen: Huyen.value,
        Tinh: Tinh.value,
        GioiTinh: GioiTinh.value,
        MaTheBH: MaTheBH.value,
        CanCuocCongDan: CanCuocCongDan.value,
        NgayVao: NgayVaoVien.value,
        NgayRa: NgayRaVien.value,
        ChanDoanVaoVien: ChanDoanVaoVien.value,
        ChanDoanRaVien: ChanDoanRaVien.value,
        LyDoVaoVien: LyDoVaoVien.value,
        TienSuBenh: TienSuBenh.value,
        DauHieuLamSang: DauHieuLamSang.value,
        QuaTrinhBenhLy: QuaTrinhBenhLy.value,
        PhuongPhapDieuTriNoi: PPDieuTriNoi,
        PhuongPhapDieuTriNgoai: PPDieuTriNgoai,
        TinhTrangRaVien: TinhTrangRaVien.value,
        KetQuaXN: ketQuaXN.value,
        NoiSoi: NoiSoi.value,
        SieuAm: SieuAm.value,
        Xquang: Xquang.value,
        DienTim: DienTim.value,
        MRI: MRI.value,
        Scanner: Scanner.value,
        PET: PET.value,
        GiaiPhauBenh: GiaiPhauBenh.value,
        HuongDieuTri: HuongDieuTri.value,
        KetQuaDieuTri: KQDT,
        NgayIn:
            'Hà Nội, ngày ' +
            new Date().getDate() +
            ' tháng ' +
            (new Date().getMonth() + 1 < 3 ? '0' : '') +
            (new Date().getMonth() + 1) +
            ' năm ' +
            new Date().getFullYear(),
        NguoiSaoBenhAn: localStorage.getItem('fullname'),
        ThuaLenh1: thuaLenh1,
        ThuaLenh2: thuaLenh2,
        NguoiKy: nguoiKy
    }
    if(soHoSoXN.value){
        data.SoHSXetNghiem = "(Mã HS: " + soHoSoXN.value +")"
    }
    else {
        data.SoHSXetNghiem = ""
    }
    // Điền dữ liệu vào mẫu Word
    doc.setData(data)

    // Kết xuất mẫu Word đã điền dữ liệu
    doc.render()

    const out = doc.getZip().generate({
        type: 'blob',
        mimeType: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
    })

    // Output the document using Data-URI
    //saveAs(out, MaKhoa.value + ' ' + HoTen.value + ' ' + soHoSo.value + '.docx')
}

const resetValues = () => {
    MaBN.value = ''
    SoLuu.value = ''
    Khoa.value = ''
    MaKhoa.value = null
    MaKhoa2.value = null
    MaYTe.value = ''
    HoTen.value = ''
    NgaySinh.value = ''
    Tuoi.value = ''
    Xa.value = ''
    Huyen.value = ''
    Tinh.value = ''
    GioiTinh.value = ''
    DiaChi.value = ''
    MaTheBH.value = ''
    CanCuocCongDan.value = ''
    NgayVaoVien.value = ''
    NgayRaVien.value = ''
    ChanDoanVaoVien.value = ''
    ChanDoanRaVien.value = ''
    PPDieuTri.value = ''
    TinhTrangRaVien.value = ''
    QuaTrinhBenhLy.value = ''
    LyDoVaoVien.value = ''
    TienSuBenh.value = ''
    DauHieuLamSang.value = ''
    patientDetail.value = ''
    benhAnNoiTru.value = ''
    ketQuaXN.value = null
    SieuAm.value = ''
    NoiSoi.value =''
    Xquang.value = ''
    DienTim.value = ''
    MRI.value = ''
    PET.value = ''
    Scanner.value = ''
    GiaiPhauBenh.value = ''
    HuongDieuTri.value = ''
    soHoSoXN.value = ''
}

</script>

<template>
    <Fluid>
        <div class="card flex flex-col gap-6 w-full">
        <div class="flex flex-col md:flex-row">
            <div class="grid grid-cols-12 gap-8">
                <div class="flex flex-wrap col-span-2 gap-2 w-full">
                    <div class="card flex justify-center">
                        <FloatLabel>
                            <InputText id="username" v-model="value" />
                            <label for="username">Số hồ sơ <span class="text-red-500">*</span></label>
                        </FloatLabel>
                    </div>
                </div>
                <div class="flex flex-wrap col-span-1 gap-2 w-full">
                    <Button icon="pi pi-search" class="mt-8" rounded outlined aria-label="Search" />
                </div>
                <div class="flex flex-wrap col-span-7 gap-2 w-full">
                    <div class="card flex justify-center">
                        <h1 class="text-3xl font-bold text-center">BẢN TÓM TẮT HỒ SƠ BỆNH ÁN</h1>
                    </div>
                </div>
                <div class="flex flex-wrap col-span-2 gap-2 w-full">
                    <div class="card flex justify-center">
                        <FloatLabel>
                            <InputText id="username" v-model="value" />
                            <label for="username">Số sao lưu<span class="text-red-500">*</span></label>
                        </FloatLabel>
                    </div>
                </div>
            </div>
        </div>

        <div class="flex flex-col md:flex-row gap-8">
            <div class="grid grid-cols-12 gap-8">
                <div class="flex flex-wrap col-span-2 gap-2 w-full">
                    <label for="sex">{{ $tt('storage_component.label.sex') }}<span class="text-red-500">*</span></label>
                    <Dropdown id="sex" editable option-value="name" optionLabel="name"></Dropdown>
                </div>
                <div class="flex flex-wrap col-span-2 gap-2 w-full">
                    <label for="birthday">{{ $tt('storage_component.label.birthday') }}<span class="text-red-500">*</span></label>
                    <InputText id="birthday" />
                </div>
                <div class="flex flex-wrap col-span-3 gap-2 w-full">
                    <label for="inDate">{{ $tt('storage_component.label.in-date') }}<span class="text-red-500">*</span></label>
                    <DatePicker id="inDate" dateFormat="dd/mm/yy" />
                </div>
                <div class="flex flex-wrap col-span-3 gap-2 w-full">
                    <label for="outDate">{{ $tt('storage_component.label.out-date') }}<span class="text-red-500">*</span></label>
                    <DatePicker id="outDate" dateFormat="dd/mm/yy" />
                </div>
                <div class="flex flex-wrap col-span-2 gap-2 w-full">
                    <label for="saveYear">{{ $tt('storage_component.label.save-year') }}<span class="text-red-500">*</span></label>
                    <InputNumber id="saveYear" :useGrouping="false" />
                </div>
            </div>
        </div>

        <div class="flex flex-col md:flex-row gap-8">
            <div class="flex flex-wrap gap-2 w-full">
                <label for="diagnose">{{ $tt('storage_component.label.diagnose') }}<span class="text-red-500">*</span></label>
                <Textarea id="diagnose" />
            </div>
        </div>
        </div>
    </Fluid>
</template>

<style lang="scss" scoped></style>
