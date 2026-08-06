import { useContext } from "react"
import { NotificationContext } from "./NotificationToast"

export const useNotification = () => {
    const {notify} = useContext(NotificationContext)

    return {notify}
}
