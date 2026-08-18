import { PrimaryButton } from "@/components/common/input/button/Button"
import { Icon } from "@iconify/react"
import { Dispatch, SetStateAction } from "react"

type TableHeaderProps = {
    setIsBarOpen: Dispatch<SetStateAction<boolean>>
}

export const TableHeader = ({setIsBarOpen}: TableHeaderProps) => {

    return (<div>
        <PrimaryButton
            label={<p className="text-xl flex gap-1 items-center">
                <Icon icon={'basil:add-outline'} />Add</p>}
            onClick={() => setIsBarOpen(true)} />
    </div>)
}
