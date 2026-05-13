import { CatalogoEnum } from "./CatalogoEnum";

export interface CatalogoItem {
    id: number;
    nombre: string;
    descripcion: string;
    estado: boolean;
    tipo: CatalogoEnum;
}