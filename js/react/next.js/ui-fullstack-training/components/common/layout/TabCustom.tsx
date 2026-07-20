import { TabGroup, TabList, Tab, TabPanels, TabPanel } from "@headlessui/react"
import { ReactNode } from "react"

type TabConfig = {
    title: string | ReactNode,
    content: () => ReactNode
}

type TabCustomProps = {
    tabs: TabConfig[]
}

export const TabCustom = ({tabs}: TabCustomProps) => {

    return (<>
        <TabGroup className={'h-full'}>
            <TabList>
                {
                    tabs.map((t, idx) => <Tab key={idx} className='mx-3'>{t.title}</Tab>)
                }
            </TabList>
            <TabPanels className={'h-full'}>
                {
                    tabs.map((t, idx) => <TabPanel className={'h-full mt-5'} key={idx}>{t.content()}</TabPanel>)
                }
            </TabPanels>
        </TabGroup>
    </>)
}
