import { BoardColumn } from './models';

export function orderedActiveColumns(columns: BoardColumn[] = []): BoardColumn[] {
  return columns
    .filter((column) => column.active)
    .map((column, index) => ({ column, index }))
    .sort((left, right) => {
      const positionDifference = left.column.posicao - right.column.posicao;
      return positionDifference || left.index - right.index;
    })
    .map(({ column }) => column);
}
