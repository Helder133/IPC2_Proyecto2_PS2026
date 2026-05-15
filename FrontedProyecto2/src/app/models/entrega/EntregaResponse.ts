import { EnumEntrega } from "./EnumEntrega";

export interface EntregaResponse {
    entregaId: number;
    contratoId: number;
    descripcion: string;
    archivo: string;
    estado: EnumEntrega;
    motivo_rechazo?: string;
    fecha: string;
}