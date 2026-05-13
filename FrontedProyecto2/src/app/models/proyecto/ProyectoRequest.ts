import { ProyectoHabilidadRequest } from "./ProyectoHabilidadRequest";

export interface ProyectoRequest {
    usuarioId: number;
    categoriaId: number;
    titulo: string;
    descripcion: string;
    presupuesto: number;
    fechaCreacion: string;
    fechaLimite: string;
    proyectoHabilidadRequest: ProyectoHabilidadRequest[];
}