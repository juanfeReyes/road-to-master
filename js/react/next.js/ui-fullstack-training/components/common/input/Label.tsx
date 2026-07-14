import { ReactNode } from "react"

type LabelProps = {
    label?: ReactNode,
    content: ReactNode
}

export const Label = ({ label, content }: LabelProps) => {

    return (
        <div className="">
            {label && <p className="text-sm font-semibold text-gray-500">{label}</p>}
            <div className="px-2 bg-gray-50 rounded-3xl border-2 border-gray-100 focus-within:border-blue-100 focus-within:shadow-blue-50">
                {content}
            </div>
        </div>
    )
}
