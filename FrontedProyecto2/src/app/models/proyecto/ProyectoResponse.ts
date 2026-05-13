import { CategoriaResponse } from "../categoria/CategoriaResponse";
import { HabilidadResponse } from "../habilidad/HabilidadResponse";
import { EnumProyecto } from "./EnumProyecto";

export interface ProyectoResponse {
    proyectoId: number;
    usuarioId: number;
    categoriaId: number;
    titulo: string;
    descripcion: string;
    presupuesto: number;
    fechaCreacion: string;
    fechaLimite: string;
    habilidadResponses: HabilidadResponse[];
    categoria: CategoriaResponse;
    estado: EnumProyecto;
}