import { Menu } from "@/components/features/menu/Menu";
import { PropsWithChildren } from "react";

type DashboardLayoutProps = {}

const DashboardLayout = ({ children }: PropsWithChildren<DashboardLayoutProps>) => {
    return (<div className="grid md:grid-cols-6 p-2 h-screen">
        <div>
            <Menu />
        </div>
        <main className="md:col-start-2 col-span-5 border-2 rounded-lg border-gray-300">{children}</main>
    </div>)
}

export default DashboardLayout;
