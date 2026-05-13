import { EnumFreelancer } from "./EnumFreelancer";

export interface FreelancerUpdate {
    usuarioId: number;
    descripcion: string;
    experiencia: EnumFreelancer;
}