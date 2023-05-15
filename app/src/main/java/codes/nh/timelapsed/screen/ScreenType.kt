package codes.nh.timelapsed.screen

import codes.nh.timelapsed.R

enum class ScreenType(val title: String, val action: String, val iconId: Int) {
    MENU_TIMELAPSE("Timelapse Menu", "", R.drawable.icon_menu),
    CREATE_TIMELAPSE("Create Timelapse", "Create", R.drawable.icon_create),
    DELETE_TIMELAPSE("Delete Timelapse", "Delete", R.drawable.icon_delete),
    START_TIMELAPSE("Start Timelapse", "Start", R.drawable.icon_start),
    STOP_TIMELAPSE("Stop Timelapse", "Stop", R.drawable.icon_stop),
    PREVIEW_TIMELAPSE("Preview Timelapse", "Preview", R.drawable.icon_preview),
    EXPORT_TIMELAPSE("Export Timelapse", "Export", R.drawable.icon_export),
}