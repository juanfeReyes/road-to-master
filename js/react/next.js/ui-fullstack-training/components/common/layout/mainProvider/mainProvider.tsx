import { PropsWithChildren } from "react"
import { SkeletonTheme } from "react-loading-skeleton"
import { CustomClientProvider } from "../../CustomClientProvider"
import { NotificationToast } from "../../interactivity/NotificationToast"


export const MainProvider = ({children}: PropsWithChildren) => {

    return (
        <CustomClientProvider>
          <NotificationToast>
            <SkeletonTheme>
              {children}
            </SkeletonTheme>
          </NotificationToast>
        </CustomClientProvider>
    )
}
