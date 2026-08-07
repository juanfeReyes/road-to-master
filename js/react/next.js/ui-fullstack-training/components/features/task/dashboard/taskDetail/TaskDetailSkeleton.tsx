import Skeleton from "react-loading-skeleton"

export const TaskDetailSkeleton = () => {
    const tableRows = 5

    return (<div className="flex flex-col gap-3 p-2">
        <div className="text-2xl flex flex-col gap-4">
            <Skeleton />
            <Skeleton />
        </div>
        <div className="h-full overflow-auto flex gap-2">
            <Skeleton width={100}/>
            <Skeleton width={100}/>
        </div>
        <div className="flex gap-2">
            <Skeleton containerClassName="w-1/4 md:w-1/4" height={20} />
            <Skeleton containerClassName="w-1/4 md:w-1/4" height={20} />
            <Skeleton containerClassName="w-1/4 md:w-1/4" height={20} />
            <Skeleton containerClassName="w-1/4 md:w-1/4" height={20} />
        </div>
        <div className="flex gap-2">
            <Skeleton containerClassName="w-1/4 md:w-1/4" height={20} count={tableRows}/>
            <Skeleton containerClassName="w-1/4 md:w-1/4" height={20} count={tableRows}/>
            <Skeleton containerClassName="w-1/4 md:w-1/4" height={20} count={tableRows}/>
            <Skeleton containerClassName="w-1/4 md:w-1/4" height={20} count={tableRows}/>
        </div>
    </div>)
}