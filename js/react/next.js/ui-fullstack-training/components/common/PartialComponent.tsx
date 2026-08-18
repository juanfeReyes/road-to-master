
export const PartialComponent = (Component, fixedProps) => {
    return (runtimeProps) => <Component {...fixedProps}  {...runtimeProps}/>;
}
