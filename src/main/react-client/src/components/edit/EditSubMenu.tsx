/* eslint-disable no-underscore-dangle */
import React, { FC } from 'react'
import { Button } from 'reactstrap'
import EditDataName from './EditDataName'
import DeleteButton from '../buttons/DeleteButton'
import EditItemListContainer from './EditItemListContainer'
import { useAppSelector } from '../../redux/hooks'
import EventBus from '../../common/EventBus'

const EditSubMenu: FC = () => {
    const { selected } = useAppSelector((state) => state.subMenu)
    const classificationId = useAppSelector(
        (state) => state.classification.selected._id
    )
    const { _id, name, itemList } = selected

    const dispatchCancel = () => {
        EventBus.dispatch('cancel')
    }

    return (
        <div>
            <span style={{ textTransform: 'uppercase' }}>{name}</span>
            <Button outline color="danger" onClick={dispatchCancel}>
                <span>Cancel</span>
            </Button>
            <DeleteButton
                id={`${classificationId}/${_id}`}
                type="submenu"
                title="Delete"
            />
            <EditDataName
                id={`${classificationId}/${_id}`}
                type="submenu"
                currentName={name}
            />
            <EditItemListContainer
                listItems={itemList}
                classificationId={classificationId}
                subMenuId={_id}
            />
        </div>
    )
}

export default EditSubMenu
