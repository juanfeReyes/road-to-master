import Skeleton from "react-loading-skeleton"

export const UserTagSkeleton = () => {

    return (<div className="flex flex-row items-center gap-1 border-2 rounded-2xl border-gray-200">
        <div>
            <Skeleton circle />
        </div>
        <div className="hidden lg:block ">
            <Skeleton />
            <Skeleton />
        </div>
    </div>)
}
