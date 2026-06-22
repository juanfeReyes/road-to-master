import { Icon } from "@iconify/react"

export const SearchBar = () => {

    return (
        <div className="flex items-center gap-2 focus-within:border-2 rounded-lg">
            <button className="text-2xl">
                <Icon icon='material-symbols:search' />
            </button>
            <input className="border-none outline-none" />
        </div>
    )
}
