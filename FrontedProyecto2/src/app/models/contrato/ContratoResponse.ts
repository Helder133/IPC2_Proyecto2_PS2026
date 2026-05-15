import { EnumContrato } from "./EnumContrato";

export interface ContratoResponse {
    contratoId: number;
    propuestaId: number;
    estado: EnumContrato;
    motivoCancelacion?: string;
    fechaCreacion: string;
    fechaFinalizacion?: string;
    comentario?: string;
    calificacion?: number;
    tituloProyecto?: string;
}