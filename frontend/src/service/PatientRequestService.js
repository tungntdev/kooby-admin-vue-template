import BaseService from '@/service/BaseService';

export default class PatientRequestService extends BaseService {
    static INSTANCE =  new PatientRequestService()

    async getPatientRequests(params){
        return await this.request(
            {
                path: '/secure/patientRequest/search',
                method: 'POST',
                data: params
            },
            {
                secure: true,
                notifyOnSuccess: false,
                notifyOnError: true
            }
        )
    }
}
