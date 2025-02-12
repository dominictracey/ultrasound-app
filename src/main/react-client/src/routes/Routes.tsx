import React, { FC, lazy, Suspense } from 'react'
import { Route, Switch } from 'react-router-dom'
import SyncLoader from 'react-spinners/SyncLoader'
import { IAppUser } from '../schemas'
import Dashboard from '../containers/Dashboard'
import { useAppSelector } from '../redux/hooks'

const Home = lazy(() => import('../containers/Home'))
const Login = lazy(() => import('../containers/Login'))
const ProtectedRoute = lazy(() => import('./ProtectedRoute'))
const Register = lazy(() => import('../containers/Register'))

const Routes: FC = () => {
    const { isAuth, user } = useAppSelector((state) => state.auth)

    const isUser = (value: unknown): value is IAppUser => {
        return !!value && !!(value as IAppUser)
    }
    const isAdmin = isUser(user) && user.roles?.includes('ROLE_ADMIN')

    return (
        <Suspense
            fallback={
                <div className="spinner">
                    <SyncLoader />
                </div>
            }
        >
            <Switch>
                <Route exact path={['/', '/home']} component={Home} />
                <Route exact path="/login" component={Login} />
                <Route exact path="/register" component={Register} />
                <ProtectedRoute
                    isAuthenticated={isAuth}
                    path="/dashboard"
                    authenticationPath="/login"
                    component={Dashboard}
                />
                <ProtectedRoute
                    isAuthenticated={isAdmin}
                    path="/admin/dashboard"
                    authenticationPath="/login"
                    component={Dashboard}
                />
                )
            </Switch>
        </Suspense>
    )
}

export default Routes
