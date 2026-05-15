import { EnumNueva_h_cTipo } from "./EnumNueva_h_cTipo";

export interface Nueva_h_cRequest {
    usuarioId: number;
    nombre: string;
    descripcion: string;
    tipo: EnumNueva_h_cTipo;
}