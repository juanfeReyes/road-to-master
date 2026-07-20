import { Header } from "@/components/common/layout/Header"
import Link from "next/link"

const navigationOptions = [
    {
        label: 'Home',
        link: '/',
        icon: 'material-symbols:home'
    },
    {
        label: 'My tasks',
        link: '/tasks',
        icon: 'material-symbols:task'
    },
    {
        label: 'My Projects',
        link: '/projects',
        icon: 'eos-icons:project'
    },
    {
        label: 'Reports & Analytics (Coming soon)',
        link: '/analytics',
        icon: 'ion:analytics'
    }
]

export const NavigationBar = () => {

    return (
        <div className="flex flex-col gap-3">
            {navigationOptions.map((opt) => (
                <Link key={opt.link} href={opt.link}>
                <Header icon={opt.icon} label={opt.label} />
            </Link>))}
        </div>
    )
}
