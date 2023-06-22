interface DynamicTableProps {
  tableTitle: string;
  headers: string[];
  body: any[];
  buildBodyRow: (row: any) => JSX.Element;
}

export const DynamicTable = ({
  tableTitle,
  headers,
  body,
  buildBodyRow,
}: DynamicTableProps) => {
  return (
    <>
      <h2 className="pt-3 pb-2 text-xl font-light">{tableTitle}</h2>
      {headers.length > 0 && (
        <table className="table-fixed rounded-xl shadow-md w-full text-center">
          <thead>
            <tr className="bg-slate-600 text-white rounded-xl">
              {headers.map((header) => (
                <th
                  key={header}
                  className="px-2 py-1 first:rounded-tl-xl last:rounded-tr-xl"
                >
                  {header}
                </th>
              ))}
            </tr>
          </thead>

          <tbody>{body.map((row) => buildBodyRow(row))}</tbody>
        </table>
      )}
    </>
  );
};
