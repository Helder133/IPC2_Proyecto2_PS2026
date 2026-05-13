import { EnumFreelancer } from "./EnumFreelancer";
import { FreelancerHabilidadRequest } from "./FreelancerHabilidadRequest";

export interface FreelancerRequest {
    usuarioId?: number;
    descripcion: string;
    experiencia: EnumFreelancer;
    tarifaHora: number;
    habilidadesRequest: FreelancerHabilidadRequest[];
}