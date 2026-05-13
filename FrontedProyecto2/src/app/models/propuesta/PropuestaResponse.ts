import { EnumPropuesta } from "./EnumPropuesta";
import { PropuestaRequest } from "./PropuestaRequest";

export interface PropuestaResponse extends PropuestaRequest {
    propuestaId: number;
    proyectoTitulo?: string;
    estado: EnumPropuesta;
}