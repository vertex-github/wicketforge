/*
 * Copyright 2013 The WicketForge-Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicketforge.search;

import com.intellij.lang.properties.PropertiesUtil;
import com.intellij.lang.properties.psi.PropertiesFile;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.util.indexing.ID;
import com.intellij.util.xml.NanoXmlUtil;
import com.intellij.util.xml.XmlFileHeader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PropertiesIndex extends WicketResourceIndexExtension {
    public static final ID<String, Void> NAME = ID.create("WicketPropertiesIndex");

    @NotNull
    @Override
    public ID<String, Void> getName() {
        return NAME;
    }

    @Override
    public boolean acceptInput(VirtualFile file) {
        FileType fileType = file.getFileType();
        if (StdFileTypes.PROPERTIES.equals(fileType)) {
            return true;
        }
        if (StdFileTypes.XML.equals(fileType)) {
            XmlFileHeader fileHeader = NanoXmlUtil.parseHeader(file);
            //noinspection ConstantConditions
            if (fileHeader != null && "properties".equals(fileHeader.getRootTagLocalName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns all properties files for the passed PsiClass.
     *
     * @param psiClass PsiClass
     * @return all properties or empty array if no such file exists.
     */
    @NotNull
    public static PsiFile[] getAllFiles(@NotNull final PsiClass psiClass) {
        return getFilesByClass(NAME, psiClass, true);
    }

    /**
     * Returns the base properties file for the passed PsiClass.
     *
     * @param psiClass PsiClass
     * @return the base properties or null if no such file exists.
     */
    @Nullable
    public static PropertiesFile getBaseFile(@NotNull final PsiClass psiClass) {
        PsiFile[] files = getFilesByClass(NAME, psiClass, false);
        return files.length > 0 ? PropertiesUtil.getPropertiesFile(files[0]) : null;
    }
}
