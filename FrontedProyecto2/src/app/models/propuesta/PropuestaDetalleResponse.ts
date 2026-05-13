import { EnumPropuesta } from "./EnumPropuesta";

export interface PropuestaDetalleResponse {
    propuestaId: number;
    estado: EnumPropuesta;
    proyectoId: number;
    usuarioId: number;
    monto: number;
    tiempoEntrega: number;
    descripcion: string;
    fechaCreacion: string;
    nombreCompleto: string;
    userName: string;
    promedioCalificacion: number;
    totalCalificacion: number;
    proyectoTitulo?: string;
}