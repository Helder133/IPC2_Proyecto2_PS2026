import { HabilidadResponse } from "../../habilidad/HabilidadResponse";
import { EnumFreelancer } from "./EnumFreelancer";

export interface FreelancerResponse {
    usuarioId: number;
    descripcion: string;
    experiencia: EnumFreelancer;
    tarifaHora: number;
    habilidadResponses?: HabilidadResponse[];
}