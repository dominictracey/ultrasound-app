/* eslint-disable react/prop-types */
import React, { FC } from 'react'
import Logout from '../buttons/LogoutButton'
import UserInfoHeader from '../UserInfoHeader'
import { useAppSelector } from '../../redux/hooks'

const Header: FC = () => {
    const { user } = useAppSelector((state) => state.auth)
    return (
        <div className="header">
            <div className="button-wrapper">
                {/* <div className="header-date">{content}</div> */}
                <Logout />
                <UserInfoHeader user={user} />
                {/* <SearchBar /> */}
            </div>
        </div>
    )
}

export default Header
