
interface DynamicTableProps {
  tableTitle: string;
  headers: string[];
  body: any[];
  buildBodyRow: (row: any) => JSX.Element
}

export const DynamicTable = ({
  tableTitle,
  headers,
  body,
  buildBodyRow
}: DynamicTableProps) => {
  return (
    <>
      <h2>{tableTitle}</h2>
      <table className="table-auto">
        <tr>
          {headers.map((header) => (
            <th className="px-2 py-1">{header}</th>
          ))}
        </tr>
        {body.map((row) => (
          buildBodyRow(row)
        ))}
      </table>
    </>
  );
};
