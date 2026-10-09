export interface BoardColumn {
  id?: number;
  nome: string;
  active: boolean;
  createdAt?: string;
  updatedAt?: string;
  posicao: number;
}

export interface Board {
  id: number;
  name: string;
  description: string;
  active: boolean;
  createdAt: string;
  updatedAt: string;
  columns: BoardColumn[];
}

export interface Card {
  id?: number;
  titulo: string;
  descricao: string;
  columnBoardId: number;
}

export interface CreateCard {
  titulo: string;
  descricao: string;
}

export interface CreateBoard {
  id?: number;
  name: string;
  description: string;
  active: boolean;
  columns: Array<{
    id?: number;
    nome: string;
    boardId?: number;
    active: boolean;
    posicao: number;
  }>;
}

interface LoginResponse {
  token: string;
  tokenType: string;
}

export type { LoginResponse };
