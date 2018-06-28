package gr.aueb.as.exporter.util;

public abstract class Constants {

    public static final String MV_GETOPT = "getopt_long@libc.so.6(1, [  ], \"bfint:uvS:TZ\", [ { \"backup\", 2, nil, 98 }, { \"context\", 0, nil, 90 }, { \"force\", 0, nil, 102 }, { \"interactive\", 0, nil, 105 }, { \"no-clobber\", 0, nil, 110 }, { \"no-target-directory\", 0, nil, 84 }, { \"strip-trailing-slashes\", 0, nil, 128 }, { \"suffix\", 1, nil, 83 }, { \"target-directory\", 1, nil, 116 }, { \"update\", 0, nil, 117 }, { \"verbose\", 0, nil, 118 }, { \"help\", 0, nil, -130 }, { \"version\", 0, nil, -131 }, { nil, 0, nil, 0 }... ], nil) = -1\n" +
            "mv: missing file operand\n" +
            "Try 'mv --help' for more information.\n" +
            "+++ exited (status 1) +++";

    public static final String MV_INFO = "File: coreutils.info,  Node: mv invocation,  Next: rm invocation, Prev: install invocation,  Up: Basic operations\n" +
            "\n" +
            "11.4 ‘mv’: Move (rename) files\n" +
            "==============================\n" +
            "\n" +
            "‘mv’ moves or renames files (or directories).  Synopses:\n" +
            "\n" +
            "     mv [OPTION]… [-T] SOURCE DEST\n" +
            "     mv [OPTION]… SOURCE… DIRECTORY\n" +
            "     mv [OPTION]… -t DIRECTORY SOURCE…\n" +
            "\n" +
            "   • If two file names are given, ‘mv’ moves the first file to the\n" +
            "     second.\n" +
            "\n" +
            "   • If the ‘--target-directory’ (‘-t’) option is given, or failing that\n" +
            "     if the last file is a directory and the ‘--no-target-directory’\n" +
            "     (‘-T’) option is not given, ‘mv’ moves each SOURCE file to the\n" +
            "     specified directory, using the SOURCEs’ names.\n" +
            "\n" +
            "   ‘mv’ can move any type of file from one file system to another.\n" +
            "Prior to version ‘4.0’ of the fileutils, ‘mv’ could move only regular\n" +
            "files between file systems.  For example, now ‘mv’ can move an entire\n" +
            "directory hierarchy including special device files from one partition to\n" +
            "another.  It first uses some of the same code that’s used by ‘cp -a’ to\n" +
            "copy the requested directories and files, then (assuming the copy\n" +
            "succeeded) it removes the originals.  If the copy fails, then the part\n" +
            "that was copied to the destination partition is removed.  If you were to\n" +
            "copy three directories from one partition to another and the copy of the\n" +
            "first directory succeeded, but the second didn’t, the first would be\n" +
            "left on the destination partition and the second and third would be left\n" +
            "on the original partition.\n" +
            "\n" +
            "   ‘mv’ always tries to copy extended attributes (xattr), which may\n" +
            "include SELinux context, ACLs or Capabilities.  Upon failure all but\n" +
            "‘Operation not supported’ warnings are output.\n" +
            "\n" +
            "   If a destination file exists but is normally unwritable, standard\n" +
            "input is a terminal, and the ‘-f’ or ‘--force’ option is not given, ‘mv’\n" +
            "prompts the user for whether to replace the file.  (You might own the\n" +
            "file, or have write permission on its directory.)  If the response is\n" +
            "not affirmative, the file is skipped.\n" +
            "\n" +
            "   _Warning_: Avoid specifying a source name with a trailing slash, when\n" +
            "it might be a symlink to a directory.  Otherwise, ‘mv’ may do something\n" +
            "very surprising, since its behavior depends on the underlying rename\n" +
            "system call.  On a system with a modern Linux-based kernel, it fails\n" +
            "with ‘errno=ENOTDIR’.  However, on other systems (at least FreeBSD 6.1\n" +
            "and Solaris 10) it silently renames not the symlink but rather the\n" +
            "directory referenced by the symlink.  *Note Trailing slashes::.\n" +
            "\n" +
            "   The program accepts the following options.  Also see *note Common\n" +
            "options::.\n" +
            "\n" +
            "‘-b’\n" +
            "‘--backup[=METHOD]’\n" +
            "     *Note Backup options::.  Make a backup of each file that would\n" +
            "     otherwise be overwritten or removed.\n" +
            "\n" +
            "‘-f’\n" +
            "‘--force’\n" +
            "     Do not prompt the user before removing a destination file.  If you\n" +
            "     specify more than one of the ‘-i’, ‘-f’, ‘-n’ options, only the\n" +
            "     final one takes effect.\n" +
            "\n" +
            "‘-i’\n" +
            "‘--interactive’\n" +
            "     Prompt whether to overwrite each existing destination file,\n" +
            "     regardless of its permissions.  If the response is not affirmative,\n" +
            "     the file is skipped.  If you specify more than one of the ‘-i’,\n" +
            "     ‘-f’, ‘-n’ options, only the final one takes effect.\n" +
            "\n" +
            "‘-n’\n" +
            "‘--no-clobber’\n" +
            "     Do not overwrite an existing file.  If you specify more than one of\n" +
            "     the ‘-i’, ‘-f’, ‘-n’ options, only the final one takes effect.\n" +
            "     This option is mutually exclusive with ‘-b’ or ‘--backup’ option.\n" +
            "\n" +
            "‘-u’\n" +
            "‘--update’\n" +
            "     Do not move a non-directory that has an existing destination with\n" +
            "     the same or newer modification time.  If the move is across file\n" +
            "     system boundaries, the comparison is to the source time stamp\n" +
            "     truncated to the resolutions of the destination file system and of\n" +
            "     the system calls used to update time stamps; this avoids duplicate\n" +
            "     work if several ‘mv -u’ commands are executed with the same source\n" +
            "     and destination.\n" +
            "\n" +
            "‘-v’\n" +
            "‘--verbose’\n" +
            "     Print the name of each file before moving it.\n" +
            "\n" +
            "‘--strip-trailing-slashes’\n" +
            "     Remove any trailing slashes from each SOURCE argument.  *Note\n" +
            "     Trailing slashes::.\n" +
            "\n" +
            "‘-S SUFFIX’\n" +
            "‘--suffix=SUFFIX’\n" +
            "     Append SUFFIX to each backup file made with ‘-b’.  *Note Backup\n" +
            "     options::.\n" +
            "\n" +
            "‘-t DIRECTORY’\n" +
            "‘--target-directory=DIRECTORY’\n" +
            "     Specify the destination DIRECTORY.  *Note Target directory::.\n" +
            "\n" +
            "‘-T’\n" +
            "‘--no-target-directory’\n" +
            "     Do not treat the last operand specially when it is a directory or a\n" +
            "     symbolic link to a directory.  *Note Target directory::.\n" +
            "\n" +
            "‘-Z’\n" +
            "‘--context’\n" +
            "     This option functions similarly to the ‘restorecon’ command, by\n" +
            "     adjusting the SELinux security context according to the system\n" +
            "     default type for destination files.\n" +
            "\n" +
            "   An exit status of zero indicates success, and a nonzero value\n" +
            "indicates failure.\n";

    public static final String MAN_INVOCATION = "MV(1)                            User Commands                           MV(1)\n" +
            "\n" +
            "NAME\n" +
            "       mv - move (rename) files\n" +
            "\n" +
            "SYNOPSIS\n" +
            "       mv [OPTION]... [-T] SOURCE DEST\n" +
            "       mv [OPTION]... SOURCE... DIRECTORY\n" +
            "       mv [OPTION]... -t DIRECTORY SOURCE...\n" +
            "\n" +
            "DESCRIPTION\n" +
            "       Rename SOURCE to DEST, or move SOURCE(s) to DIRECTORY.\n" +
            "\n" +
            "       Mandatory  arguments  to  long  options are mandatory for short options\n" +
            "       too.\n" +
            "\n" +
            "       --backup[=CONTROL]\n" +
            "              make a backup of each existing destination file\n" +
            "\n" +
            "       -b     like --backup but does not accept an argument\n" +
            "\n" +
            "       -f, --force\n" +
            "              do not prompt before overwriting\n" +
            "\n" +
            "       -i, --interactive\n" +
            "              prompt before overwrite\n" +
            "\n" +
            "       -n, --no-clobber\n" +
            "              do not overwrite an existing file\n" +
            "\n" +
            "       If you specify more than one of -i, -f, -n, only the  final  one  takes\n" +
            "       effect.\n" +
            "\n" +
            "       --strip-trailing-slashes\n" +
            "              remove any trailing slashes from each SOURCE argument\n" +
            "\n" +
            "       -S, --suffix=SUFFIX\n" +
            "              override the usual backup suffix\n" +
            "\n" +
            "       -t, --target-directory=DIRECTORY\n" +
            "              move all SOURCE arguments into DIRECTORY\n" +
            "\n" +
            "       -T, --no-target-directory\n" +
            "              treat DEST as a normal file\n" +
            "\n" +
            "       -u, --update\n" +
            "              move  only  when  the  SOURCE file is newer than the destination\n" +
            "              file or when the destination file is missing\n" +
            "\n" +
            "       -v, --verbose\n" +
            "              explain what is being done\n" +
            "\n" +
            "       -Z, --context\n" +
            "              set SELinux security context of destination file to default type\n" +
            "\n" +
            "       --help display this help and exit\n" +
            "\n" +
            "       --version\n" +
            "              output version information and exit\n" +
            "\n" +
            "       The  backup  suffix  is  '~',  unless  set  with   --suffix   or   SIM‐\n" +
            "       PLE_BACKUP_SUFFIX.   The version control method may be selected via the\n" +
            "       --backup option or through the  VERSION_CONTROL  environment  variable.\n" +
            "       Here are the values:\n" +
            "\n" +
            "       none, off\n" +
            "              never make backups (even if --backup is given)\n" +
            "\n" +
            "       numbered, t\n" +
            "              make numbered backups\n" +
            "\n" +
            "       existing, nil\n" +
            "              numbered if numbered backups exist, simple otherwise\n" +
            "\n" +
            "       simple, never\n" +
            "              always make simple backups\n" +
            "\n" +
            "AUTHOR\n" +
            "       Written by Mike Parker, David MacKenzie, and Jim Meyering.\n" +
            "\n" +
            "REPORTING BUGS\n" +
            "       GNU coreutils online help: <http://www.gnu.org/software/coreutils/>\n" +
            "       Report mv translation bugs to <http://translationproject.org/team/>\n" +
            "\n" +
            "COPYRIGHT\n" +
            "       Copyright  ©  2014  Free Software Foundation, Inc.  License GPLv3+: GNU\n" +
            "       GPL version 3 or later <http://gnu.org/licenses/gpl.html>.\n" +
            "       This is free software: you are free  to  change  and  redistribute  it.\n" +
            "       There is NO WARRANTY, to the extent permitted by law.\n" +
            "\n" +
            "SEE ALSO\n" +
            "       rename(2)\n" +
            "\n" +
            "       Full documentation at: <http://www.gnu.org/software/coreutils/mv>\n" +
            "       or available locally via: info '(coreutils) mv invocation'\n" +
            "\n" +
            "GNU coreutils 8.23               October 2015                            MV(1)\n";
}
