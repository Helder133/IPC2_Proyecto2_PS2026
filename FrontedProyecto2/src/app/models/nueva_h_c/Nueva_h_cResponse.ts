import { EnumNueva_h_cEstado } from "./EnumNueva_h_cEstado";
import { EnumNueva_h_cTipo } from "./EnumNueva_h_cTipo";

export interface Nueva_h_cResponse {
    solicitudId: number;
    usuarioId: number;
    nombre: string;
    descripcion: string;
    tipo: EnumNueva_h_cTipo;
    estado: EnumNueva_h_cEstado;
    fechaCreacion: string;
    nombreCompleto: string;
    userName: string;
}