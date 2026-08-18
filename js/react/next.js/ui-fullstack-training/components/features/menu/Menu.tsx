import { MenuFooter } from "./MenuFooter"
import { NavigationBar } from "./NavigationBar"
import { UserTag } from "./UserTag/UserTag"

export const Menu = () => {
    return (<div className="flex flex-col gap-4 px-2 justify-between content-between h-full">
        <div className="flex flex-col gap-2">
            <div><UserTag /></div>
            <div className="hidden md:block"><NavigationBar /></div>
        </div>

        <div className="hidden md:block"><MenuFooter /></div>
    </div>)
}